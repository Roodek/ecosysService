package com.eco.ecosystem.controllers;

import com.eco.ecosystem.controllers.requestBodies.PlayerNameBody;
import com.eco.ecosystem.controllers.requestBodies.PlayerUUIDBody;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Message;
import com.eco.ecosystem.services.GameService;
import com.eco.ecosystem.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<GameDto> getGames(){
        return gameService.getAllGames();
    }
    @PostMapping
    public Mono<GameDto> saveGame(@RequestBody Mono<GameDto> gameDtoMono){
        return gameService.saveGame(gameDtoMono);
    }

    @PostMapping("/new")
    public Mono<UUID> initGame(@RequestBody PlayerNameBody playerName){
        return gameService.initGame(playerName.getPlayerName());
    }
    @PostMapping("/{id}/join")
    public Mono<UUID> joinGame(@PathVariable UUID id,
                               @RequestBody PlayerNameBody playerName ){
        simpMessagingTemplate.convertAndSend("/topic/games", new Message(playerName.getPlayerName(),"joined"));
        return gameService.joinGame(id,playerName.getPlayerName());
    }

    @PostMapping("/{id}/leave")
    public Mono<Void> leaveGame(@PathVariable UUID id,
                               @RequestBody PlayerUUIDBody playerUUIDBody ){
        simpMessagingTemplate.convertAndSend("/topic/games", new Message(playerUUIDBody.getPlayerID(),"left"));
        return gameService.leaveGame(id,UUID.fromString(playerUUIDBody.getPlayerID()));
    }

    @PostMapping("{id}/start")
    public Mono<Void> startGame(@PathVariable UUID id){
        return gameService.startGame(id).then();

    }
    @GetMapping("/{id}")
    public Mono<GameDto> getGame(@PathVariable UUID id){
        return gameService.getGame(id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteGame(@PathVariable UUID id){
        return gameService.deleteGame(id);
    }





}
