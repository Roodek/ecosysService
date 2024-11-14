package com.eco.ecosystem.services;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Message;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.game.exceptions.FullPlayerCountException;
import com.eco.ecosystem.game.exceptions.GameNotFoundException;
import com.eco.ecosystem.repository.GameRepository;
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

import java.util.List;
import java.util.UUID;

@Service
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
        return gameRepository.findById(id).map(AppUtils::gameEntityToDto);
    }

    public Mono<GameDto> saveGame(Mono<GameDto> productDtoMono) {
        return productDtoMono.map(AppUtils::gameDtoToEntity)
                .flatMap(gameRepository::insert)
                .map(AppUtils::gameEntityToDto);
    }

    public void swapPlayerHands(UUID gameID) {
        gameRepository.findById(gameID)
                .map(AppUtils::gameEntityToDto)
                .map(GameDto::swapPlayersHands)
                .map(AppUtils::gameDtoToEntity)
                .flatMap(gameRepository::save)
                .ignoreElement();
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
        var newPlayer = new Player(newPlayeruuid, playerName, List.of(), List.of(List.of()), 0);
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

    public Mono<GameDto> endGame(UUID gameID) {
        return null;
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
                            .filter(player -> player.getId() != playerID)
                            .forEach(player -> player.setCardsInHand(List.of()));
                    return Mono.just(gameDto);
                });
    }

    public Mono<GameDto> updateGameStateIfTurnEnded(UUID gameID) {
        return getGame(gameID)
                .flatMap(gameDto -> arePlayerHandsEqualInSize(gameDto.getPlayers()) ?
                        startNewTurn(gameDto) :
                        Mono.just(gameDto));
    }

    private Mono<GameDto> startNewTurn(GameDto gameDto){
        var gameCopy = new GameDto(gameDto);
        if(gameCopy.getTurn()==HALF_GAME_TURN){
            gameCopy.startSecondPhase();
        }
        gameCopy.setTurn(gameCopy.getTurn()+1);
        gameCopy.swapPlayersHands();
        return updateGame(gameCopy,gameCopy.getId());
    }

    private boolean arePlayerHandsEqualInSize(List<Player> players) {
        var expectedSize = players.get(0).getCardsInHand().size();
        return players.stream().allMatch(player -> player.getCardsInHand().size() == expectedSize);
    }
}
