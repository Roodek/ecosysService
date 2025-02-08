package com.eco.ecosystem.dto;

import com.eco.ecosystem.entities.FinalGeneralCount;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.CardStack;
import com.eco.ecosystem.game.GameTable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import utils.Timestamp;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static com.eco.ecosystem.services.GameService.HALF_GAME_TURN;


@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class GameDto{

    @Id
    UUID id;
    @JsonProperty("players")
    List<Player>players;
    @JsonProperty("cardStack")
    List<PlayerCard> cardStack;
    @JsonProperty("turn")
    int turn;
    @JsonProperty("createdAt")
    Timestamp createdAt;

    public GameDto(GameDto gameDto) {
        this.id = gameDto.getId();
        this.players = gameDto.getPlayers();
        this.cardStack = gameDto.cardStack;
        this.turn = gameDto.turn;
        this.createdAt = gameDto.getCreatedAt();
    }

    public GameDto(UUID id, List<Player> players, List<PlayerCard> cardStack, int turn) {
        this.id = id;
        this.players = players;
        this.cardStack = cardStack;
        this.turn = turn;
        this.createdAt = Timestamp.now();
    }

    public boolean allPlayersAppliedMove(){
        return players.stream().allMatch(player -> player.getBoard().stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull).count()==turn);
    }
    public GameDto swapPlayersHands() {
        if (turn <= HALF_GAME_TURN) {
            List<PlayerCard> swapped = players.get(0).getCardsInHand();
            List<PlayerCard> buffer;
            for (int i = players.size() - 1; i >= 0; i--) {
                buffer = players.get(i).getCardsInHand();
                players.get(i).setCardsInHand(swapped);
                swapped = buffer;
            }
        } else {
            List<PlayerCard> swapped = players.get(players.size() - 1).getCardsInHand();
            List<PlayerCard> buffer;
            for (int i = 0; i < players.size(); i++) {
                buffer = players.get(i).getCardsInHand();
                players.get(i).setCardsInHand(swapped);
                swapped = buffer;
            }
        }
        return this;
    }

    public GameDto startGame(){
        cardStack = CardStack.initCardStack().stream().map(PlayerCard::new).toList();
        turn=1;
        players.forEach(player-> player.setCardsInHand(dealCards(11)));
        return this;
    }

    public GameDto startSecondPhase(){
        players.forEach(player-> player.setCardsInHand(
                Stream.concat(player.getCardsInHand().stream(),
                        dealCards(10).stream()).toList())
        );
        return this;
    }

    private List<PlayerCard> dealCards(int numberOfCards){
        var cardsDealt = cardStack.subList(0,numberOfCards);
        cardStack = cardStack.subList(numberOfCards,cardStack.size());
        return cardsDealt;
    }
    public GameDto endGame(){
        var playersWithCalculatedPoints = new GameTable(players.stream()
                .map(Player::toGamePlayer).toList())
                .endGame();
        for(int i=0;i<players.size();i++){
            players.get(i).setPointCount(playersWithCalculatedPoints.get(i).getSumOfPoints());
            players.get(i).setFinalGeneralPointCount(
                    new FinalGeneralCount(
                            playersWithCalculatedPoints.get(i).getGeneralPointCount(),
                            playersWithCalculatedPoints.get(i).getNumberOfGaps(),
                            playersWithCalculatedPoints.get(i).getEcosystemGapPoints()
                            ));
        }
        return this;
    }
}
