package com.eco.ecosystem.repository;

import com.eco.ecosystem.controllers.requestBodies.PlayerUpdateRequestBody;
import com.eco.ecosystem.controllers.responseObjects.AvailableMovesResponse;
import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.board.BoardAvailableMoveCalculator;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<UpdateResult> updatePlayersHand(UUID gameID, UUID playerID, PlayerUpdateRequestBody player) {
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID)
                        .and(Game.PLAYERS_FIELD + "." + Player.ID_FIELD).is(playerID.toString()));
        Update update = new Update()
                .set(Game.PLAYERS_FIELD + ".$." + Player.CARDS_IN_HAND_FIELD, player.getCardsInHand());

        return reactiveMongoTemplate.updateFirst(query, update, Game.class);
    }

    public Mono<Player> getPlayer(UUID gameID, UUID playerID) {
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID));

        return reactiveMongoTemplate.findOne(query, Game.class)
                .flatMap(game -> game.getPlayers().stream()
                        .filter(player -> player.getId().equals(playerID))
                        .findFirst().map(Mono::just).orElseGet(()->Mono.error(new Exception("player not found"))));
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
}
