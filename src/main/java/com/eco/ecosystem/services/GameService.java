package com.eco.ecosystem.services;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.*;
import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.cards.Card;
import com.eco.ecosystem.game.exceptions.FullPlayerCountException;
import com.eco.ecosystem.game.exceptions.GameNotFoundException;
import com.eco.ecosystem.game.exceptions.InvalidMoveException;
import com.eco.ecosystem.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.AppUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GameService {

    public static final int HALF_GAME_TURN = 10;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<GameDto> getAllGames() {
        return gameRepository.findAll().map(AppUtils::gameEntityToDto);
    }

    public Mono<GameDto> getGame(UUID id) {
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(id));
        return reactiveMongoTemplate.findOne(query, Game.class).map(AppUtils::gameEntityToDto);
    }

    public Mono<GameDto> saveGame(Mono<GameDto> productDtoMono) {
        return productDtoMono.map(AppUtils::gameDtoToEntity)
                .flatMap(gameRepository::insert)
                .map(AppUtils::gameEntityToDto);
    }

    public Mono<GameDto> updateGame(GameDto gameDto, UUID gameID) {
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID));
        FindAndReplaceOptions options = FindAndReplaceOptions.options().returnNew();

        return reactiveMongoTemplate.findAndReplace(query, AppUtils.gameDtoToEntity(gameDto), options, Game.COLLECTION_NAME)
                .map(AppUtils::gameEntityToDto);
    }

    public Mono<Void> deleteGame(UUID id) {
        return gameRepository.deleteById(id);
    }

    public Mono<UUID> initGame() {
        var newId = UUID.randomUUID();
        var newGame = Mono.just(new GameDto(newId, List.of(), List.of(), 0));
        return newGame.map(AppUtils::gameDtoToEntity)
                .flatMap(gameRepository::insert)
                .then(Mono.just(newId));
    }

    public Mono<UUID> joinGame(UUID gameID, String playerName) {
        var newPlayeruuid = UUID.randomUUID();
        var newPlayer = new Player(newPlayeruuid, playerName, List.of(), null, List.of(List.of()), 0);
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID));
        Update update = new Update()
                .push(Game.PLAYERS_FIELD, newPlayer);
        System.out.println("player: " + newPlayeruuid + " joined");
        return validateGameExistsAndGet(gameID).flatMap(gameDto -> gameDto.getPlayers().size() < 6 ?
                reactiveMongoTemplate.updateFirst(query, update, Game.class)
                        .flatMap(updateResult -> updateResult.getMatchedCount() == 0 ?
                                Mono.error(new GameNotFoundException()) : Mono.just(newPlayeruuid)) :
                Mono.error(new FullPlayerCountException()));
    }

    public Mono<Void> leaveGame(UUID gameID, UUID playerID) {
        System.out.println("player: " + playerID + " left");
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID));
        Update update = new Update().pull("players", new Query(Criteria.where(Player.ID_FIELD).is(playerID)));

        return reactiveMongoTemplate.updateFirst(query, update, Game.class)
                .then(deleteGameIfNoPlayersLeft(gameID));
    }

    @NotNull
    private Mono<Void> deleteGameIfNoPlayersLeft(UUID gameID) {
        return validateGameExistsAndGet(gameID)
                .flatMap(gameDto -> gameDto.getPlayers().isEmpty() ?
                        deleteGame(gameID)
                                .then(Mono.fromRunnable(() -> simpMessagingTemplate.convertAndSend("/topic/games",
                                        new Message(gameID.toString(), "game deleted"))))
                        : Mono.empty().then());
    }


    private Mono<GameDto> validateGameExistsAndGet(UUID gameID) {
        return reactiveMongoTemplate.findById(gameID, Game.class)
                .switchIfEmpty(Mono.error(new GameNotFoundException()))
                .flatMap(game -> Mono.just(AppUtils.gameEntityToDto(game)));
    }

    public Mono<GameDto> startGame(UUID gameID) {
        return validateGameExistsAndGet(gameID)
                .flatMap(gameDto -> Mono.just(gameDto.startGame()))
                .flatMap(gameDto -> updateGame(gameDto, gameID));
    }

    public Mono<GameDto> getGameForSpecificPlayer(UUID gameID, UUID playerID) {
        return validateGameExistsAndGet(gameID)
                .flatMap(gameDto -> {
                    gameDto.getPlayers().stream()
                            .filter(player -> !player.getId().equals(playerID))
                            .forEach(player -> {
                                player.setCardsInHand(List.of());
                                if(player.getSelectedMove()!=null){
                                    player.setSelectedMove(new SelectedMove());
                                }
                            });
                    return Mono.just(gameDto);
                });
    }

    public Mono<GameDto> updateGameStateIfTurnEnded(UUID gameID) {
        return getGame(gameID)
                .flatMap(gameDto -> arePlayersCommitted(gameDto.getPlayers()) ?
                        startNewTurn(gameDto)
                                .map(updatedGame -> {
                                    sendMessageBasedOnGameTurn(gameID, updatedGame);
                                    return updatedGame;
                                }) :
                        Mono.just(gameDto));
    }

    private void sendMessageBasedOnGameTurn(UUID gameID, GameDto updatedGame) {
        if (updatedGame.getTurn() == 20) {
            simpMessagingTemplate.convertAndSend("/topic/games/" + gameID.toString(),
                    new Message(gameID.toString(), "GAME_ENDED"));
        } else {
            simpMessagingTemplate.convertAndSend("/topic/games/" + gameID.toString(),
                    new Message(gameID.toString(), "TURN_ENDED"));
        }
    }

    private Mono<GameDto> startNewTurn(GameDto gameDto) {
        var gameCopy = new GameDto(gameDto);
        gameCopy.setPlayers(
                gameCopy.getPlayers().stream()
                .map(this::processPlayerOnEndOfTurn).toList()
        );
        return gameCopy.allPlayersAppliedMove() ?
                handleEndOfTurn(gameCopy) :
                Mono.just(gameCopy);
    }

    private Player processPlayerOnEndOfTurn(Player player) {
        try {
            applySelectedMove(player);
        } catch (InvalidMoveException e) {
            log.error(e.getLocalizedMessage());
        }
        finally {
            player.setSelectedMove(null);
        }
        return player;
    }

    private Mono<GameDto> handleEndOfTurn(GameDto gameDto) {
        if (gameDto.getTurn() == HALF_GAME_TURN) {
            gameDto.startSecondPhase();
        } else {
            gameDto.swapPlayersHands();
        }
        if (gameDto.getTurn() == 20) {
            gameDto.setTurn(gameDto.getTurn() + 1);
            return updateGame(gameDto.endGame(), gameDto.getId());
        }
        gameDto.setTurn(gameDto.getTurn() + 1);
        return updateGame(gameDto, gameDto.getId());
    }

    private void applySelectedMove(Player player) throws InvalidMoveException {
        var updatedBoard = player.getSelectedMove().getSelectedCard() == Card.CardType.RABBIT
                && player.getSelectedMove().getSlotToSwap1() != null
                && player.getSelectedMove().getSlotToSwap2() != null ?
                new Board(player.getBoard())
                        .putRabbitCard(
                                player.getSelectedMove().getSelectedSlot(),
                                player.getSelectedMove().getSlotToSwap1(),
                                player.getSelectedMove().getSlotToSwap2()
                        )
                :
                new Board(player.getBoard())
                        .putCard(
                                Card.from(player.getSelectedMove().getSelectedCard()),
                                player.getSelectedMove().getSelectedSlot()
                        );
        player.setCardsInHand(removeCardFromPlayersHand(player));
        player.setBoard(updatedBoard.toResponseBoard());
    }

    private List<PlayerCard> removeCardFromPlayersHand(Player player) {
        var result = new ArrayList<>(player.getCardsInHand());
        Iterator<PlayerCard> itr = result.iterator();
        while (itr.hasNext()) {
            if (itr.next().getCardType().equals(player.getSelectedMove().getSelectedCard().toString())) {
                itr.remove();
                break;
            }
        }
        return result;
    }

    private boolean arePlayersCommitted(List<Player> players) {
        return players.stream().allMatch(player -> player.getSelectedMove() != null);
    }
}
