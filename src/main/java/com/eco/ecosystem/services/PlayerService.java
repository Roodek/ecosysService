package com.eco.ecosystem.services;

import com.eco.ecosystem.controllers.requestBodies.PlayerUpdateRequestBody;
import com.eco.ecosystem.controllers.requestBodies.PutCardRequestBody;
import com.eco.ecosystem.controllers.requestBodies.PutRabbitCardAndSwapTwoRequestBody;
import com.eco.ecosystem.controllers.responseObjects.AvailableMovesResponse;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.board.BoardAvailableMoveCalculator;
import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.Card;
import com.eco.ecosystem.game.exceptions.InvalidMoveException;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class PlayerService {

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    GameService gameService;

    public Mono<UpdateResult> updatePlayersHand(UUID gameID, UUID playerID, PlayerUpdateRequestBody player) {
        Query query = getPlayerQuery(gameID, playerID);
        Update update = new Update()
                .set(Game.PLAYERS_FIELD + ".$." + Player.CARDS_IN_HAND_FIELD, player.getCardsInHand());

        return reactiveMongoTemplate.updateFirst(query, update, Game.class);
    }

    private static Query getPlayerQuery(UUID gameID, UUID playerID) {
        return new Query(
                Criteria.where(Game.ID_FIELD).is(gameID)
                        .and(Game.PLAYERS_FIELD + "." + Player.ID_FIELD).is(playerID));
    }

    public Mono<Player> getPlayer(UUID gameID, UUID playerID) {
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID));

        return reactiveMongoTemplate.findOne(query, Game.class)
                .flatMap(game -> game.getPlayers().stream()
                        .filter(player -> player.getId().equals(playerID))
                        .findFirst().map(Mono::just).orElseGet(() -> Mono.error(new Exception("player not found"))));
    }

    public Mono<List<Player>> getPlayers(UUID gameID) {
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID));

        return reactiveMongoTemplate.findOne(query, Game.class)
                .map(Game::getPlayers);
    }

    public Mono<AvailableMovesResponse> getAvailableMoves(UUID gameID, UUID playerID) {
        return getPlayer(gameID, playerID)
                .flatMap(player -> Mono.just(
                        new AvailableMovesResponse(
                                player.getCardsInHand(),
                                new BoardAvailableMoveCalculator(new Board(player.getBoard()))
                                        .getAvailableMoves().stream().toList())
                ));
    }

    public Mono<GameDto> putCard(UUID gameID, UUID playerID, PutCardRequestBody body) {
        return getPlayer(gameID, playerID)
                .flatMap(player -> putCardOnPlayersBoard(gameID, playerID, body, player)
                        .flatMap(board -> gameService.updateGameStateIfTurnEnded(gameID).ignoreElement()
                                .then(gameService.getGameForSpecificPlayer(gameID,playerID))));
    }

    public Mono<GameDto> rabbitSwapCard(UUID gameID, UUID playerID, PutRabbitCardAndSwapTwoRequestBody body){
        return getPlayer(gameID, playerID)
                .flatMap(player -> putRabbitCardOnBoard(gameID, playerID, body, player)
                        .flatMap(board -> gameService.updateGameStateIfTurnEnded(gameID).ignoreElement()
                                .then(gameService.getGameForSpecificPlayer(gameID,playerID))
                        )
                );
    }

    private Mono<List<List<PlayerCard>>> putRabbitCardOnBoard(UUID gameID, UUID playerID, PutRabbitCardAndSwapTwoRequestBody body, Player player){
        var cardSlots = getCardsSlots(player.getBoard());
        if (
                !isMoveAvailable(player, new PlayerCard(Card.from(Card.CardType.RABBIT)), body.getRabbitSlot()) ||
                !cardSlots.contains(body.getSlotToSwap1()) ||
                !cardSlots.contains(body.getSlotToSwap2())
        ) {
            return Mono.error(new InvalidMoveException("Move invalid"));
        }
        try {
            var updatedBoard = new Board(player.getBoard())
                    .putCard(
                            Card.from(Card.CardType.RABBIT),
                            body.getRabbitSlot().coordX(),
                            body.getRabbitSlot().coordY())
                    .rabbitSwap(body.getSlotToSwap1(),body.getSlotToSwap2())
                    .toResponseBoard();
            Update update = new Update()
                    .set(Game.PLAYERS_FIELD + ".$." + Player.BOARD_FIELD, updatedBoard)
                    .set(Game.PLAYERS_FIELD + ".$." + Player.CARDS_IN_HAND_FIELD, removeCardFromPlayersHand(player, Card.CardType.RABBIT));

            return reactiveMongoTemplate.updateFirst(getPlayerQuery(gameID, playerID), update, Game.class).then(Mono.just(updatedBoard));
        } catch (InvalidMoveException e) {
            return Mono.error(e);
        }
    }

    private Mono<List<List<PlayerCard>>> putCardOnPlayersBoard(UUID gameID, UUID playerID, PutCardRequestBody body, Player player) {
        if (!isMoveAvailable(player, new PlayerCard(body.getCardType().toString()), body.getSlot())) {
            return Mono.error(new InvalidMoveException("Move invalid"));
        }
        try {
            var updatedBoard = new Board(player.getBoard())
                    .putCard(
                            Card.from(body.getCardType()),
                            body.getSlot().coordX(),
                            body.getSlot().coordY()
                    ).toResponseBoard();
            Update update = new Update()
                    .set(Game.PLAYERS_FIELD + ".$." + Player.BOARD_FIELD, updatedBoard)
                    .set(Game.PLAYERS_FIELD + ".$." + Player.CARDS_IN_HAND_FIELD, removeCardFromPlayersHand(player, body.getCardType()));

            return reactiveMongoTemplate.updateFirst(getPlayerQuery(gameID, playerID), update, Game.class).then(Mono.just(updatedBoard));
        } catch (InvalidMoveException e) {
            return Mono.error(e);
        }
    }
    private Set<Slot> getCardsSlots(List<List<PlayerCard>> board){
        Set<Slot> slots = new HashSet<>();
        for(int i=0;i<board.size();i++){
            for(int j=0;j<board.get(i).size();j++){
                if(board.get(i).get(j)!=null){
                    slots.add(new Slot(i,j));
                }
            }
        }
        return slots;
    }
    private Boolean isMoveAvailable(Player player, PlayerCard card, Slot slot) {
        return new BoardAvailableMoveCalculator(new Board(player.getBoard()))
                .getAvailableMoves().stream().toList().contains(slot)
                && player.getCardsInHand().stream().map(PlayerCard::getCardType).toList().contains(card.getCardType());
    }

    private List<PlayerCard> removeCardFromPlayersHand(Player player, Card.CardType card) {
        var result = new ArrayList<>(player.getCardsInHand());
        Iterator<PlayerCard> itr = result.iterator();
        while (itr.hasNext()) {
            if (itr.next().getCardType().equals(card.toString())) {
                itr.remove();
                break;
            }
        }
        return result;
    }
}
