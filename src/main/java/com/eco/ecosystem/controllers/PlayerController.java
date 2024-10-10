package com.eco.ecosystem.controllers;


import com.eco.ecosystem.controllers.requestBodies.PlayerUpdateRequestBody;
import com.eco.ecosystem.controllers.responseObjects.AvailableMovesResponse;
import com.eco.ecosystem.repository.PlayerService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @PostMapping(path = "/{gameID}/{playerID}")
    public Mono<UpdateResult> updatePlayer(@PathVariable String gameID,
                                           @PathVariable String playerID,
                                           @RequestBody PlayerUpdateRequestBody body){

        return playerService.updatePlayersHand(UUID.fromString(gameID),UUID.fromString(playerID),body);
    }
    @GetMapping(path = "/{gameID}/{playerID}/availableMoves")
    public Mono<AvailableMovesResponse> getAvailableMoves(@PathVariable String gameID,
                                                          @PathVariable String playerID){
        return playerService.getAvailableMoves(UUID.fromString(gameID),UUID.fromString(playerID));
    }
}
