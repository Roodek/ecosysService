package com.eco.ecosystem.controllers;

import com.eco.ecosystem.controllers.requestBodies.PlayerNameBody;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Message;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.services.GameService;
import com.eco.ecosystem.services.PlayerService;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/games")
public class GameController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public Flux<GameDto> getGames() {
        return gameService.getAllGames()
                .map(gameDto -> new GameDto(
                        gameDto.getId(),
                        gameDto.getPlayers().stream()
                                .map(player->new Player(player.getId(),player.getName(),
                                        null,
                                        null,
                                        null,0))
                                .toList(),
                        List.of(),
                        gameDto.getTurn(),
                        gameDto.getCreatedAt()
                ));
    }

    @PostMapping
    public Mono<GameDto> saveGame(@RequestBody Mono<GameDto> gameDtoMono) {
        return gameService.saveGame(gameDtoMono);
    }

    @PostMapping("/new")
    public Mono<UUID> initGame() {
        simpMessagingTemplate.convertAndSend("/topic/games", new Message("game", "created"));
        return gameService.initGame();
    }

    @PostMapping("/{id}/join")
    public Mono<UUID> joinGame(@PathVariable UUID id,
                               @RequestBody PlayerNameBody playerName) {
        simpMessagingTemplate.convertAndSend("/topic/games", new Message(playerName.getPlayerName(), "joined"));
        simpMessagingTemplate.convertAndSend("/topic/games/" + id.toString(), new Message(playerName.getPlayerName(), "joined"));
        return gameService.joinGame(id, playerName.getPlayerName());
    }

    @PostMapping("/{id}/leave")
    public Mono<Void> leaveGame(@PathVariable UUID id,
                                @RequestBody String playerUUIDBody) {
        var jsonBody = new JsonObject(playerUUIDBody);
        var playerID = jsonBody.toBsonDocument().getString("playerID").getValue();
        simpMessagingTemplate.convertAndSend("/topic/games/" + id.toString(), new Message(playerID, "left"));
        simpMessagingTemplate.convertAndSend("/topic/games", new Message(playerID, "left"));
        return gameService.leaveGame(id, UUID.fromString(playerID));
    }

    @PostMapping("{id}/start")
    public Mono<Void> startGame(@PathVariable UUID id) {
        return gameService.startGame(id).then(Mono.fromRunnable(() -> {
            simpMessagingTemplate.convertAndSend("/topic/games", new Message(id.toString(), "game"+ id + " started"));
            simpMessagingTemplate.convertAndSend("/topic/games/" + id, new Message(id.toString(), "GAME_STARTED"));
        }));

    }

    @GetMapping("/{id}")
    public Mono<GameDto> getGame(@PathVariable UUID id) {
        return gameService.getGame(id);
    }

    @GetMapping("/{id}/endCount")
    public Mono<GameDto> getGameFinalCount(@PathVariable UUID id) {
        return gameService.getGame(id).flatMap(gameDto -> Mono.just(gameDto.endGame()));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteGame(@PathVariable UUID id) {
        return gameService.deleteGame(id);
    }


}
