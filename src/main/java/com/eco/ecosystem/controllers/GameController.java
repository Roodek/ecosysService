package com.eco.ecosystem.controllers;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/games")
public class GameController {

    @Autowired
    private GameService service;

    @GetMapping
    public Flux<GameDto> getGames(){
        return service.getAllGames();
    }
    @GetMapping("/{id}")
    public Mono<GameDto> getGame(@PathVariable String id){
        return service.getGame(UUID.fromString(id));
    }

    @PostMapping
    public Mono<GameDto> saveGame(@RequestBody Mono<GameDto> gameDtoMono){
        return service.saveGame(gameDtoMono);
    }

    @PutMapping("/update/{id}")
    public Mono<GameDto> updateGame(@RequestBody Mono<GameDto> gameDtoMono, @PathVariable String id){
        return service.updateGame(gameDtoMono, UUID.fromString(id));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteGame(@PathVariable String id){
        return service.deleteGame(UUID.fromString(id));
    }
}
