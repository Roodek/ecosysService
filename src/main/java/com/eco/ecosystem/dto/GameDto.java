package com.eco.ecosystem.dto;

import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;


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

    public GameDto swapPlayersHands() {
        if (turn < 10) {
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
        players.forEach(player-> player.getCardsInHand().addAll(dealCards(11)));
        return this;
    }

    public GameDto startSecondPhase(){
        players.forEach(player-> player.getCardsInHand().addAll(dealCards(10)));
        return this;
    }

    private List<PlayerCard> dealCards(int numberOfCards){
        var cardsDealt = cardStack.subList(0,numberOfCards-1);
        cardStack = cardStack.subList(numberOfCards,cardStack.size()-1);
        return cardsDealt;
    }
}
