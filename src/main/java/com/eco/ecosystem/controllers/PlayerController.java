package com.eco.ecosystem.controllers;


import com.eco.ecosystem.controllers.requestBodies.PlayerUpdateRequestBody;
import com.eco.ecosystem.controllers.requestBodies.PutCardRequestBody;
import com.eco.ecosystem.controllers.requestBodies.PutRabbitCardAndSwapTwoRequestBody;
import com.eco.ecosystem.controllers.responseObjects.AvailableMovesResponse;
import com.eco.ecosystem.controllers.responseObjects.GameResponse;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.services.PlayerService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import utils.AppUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/games/{gameID}/players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @PostMapping(path = "/{playerID}")
    public Mono<UpdateResult> updatePlayer(@PathVariable UUID gameID,
                                           @PathVariable UUID playerID,
                                           @RequestBody PlayerUpdateRequestBody body){
        return playerService.updatePlayersHand(gameID,playerID,body);
    }
    @GetMapping(path = "/{playerID}")
    public Mono<Player> getPlayer(@PathVariable UUID gameID, @PathVariable UUID playerID){
        return playerService.getPlayer(gameID,playerID);
    }
    @GetMapping(path = "/{playerID}/availableMoves")
    public Mono<AvailableMovesResponse> getAvailableMoves(@PathVariable UUID gameID, @PathVariable UUID playerID){
        return playerService.getAvailableMoves(gameID,playerID);
    }
    @PostMapping(path = "/{playerID}/putCard")
    public Mono<GameResponse> putCard(@PathVariable UUID gameID,
                                      @PathVariable UUID playerID,
                                      @RequestBody PutCardRequestBody body){
        return playerService.putCard(gameID,playerID,body).map(AppUtils::gameDtoToResponse);
    }

    @PostMapping(path = "/{playerID}/swapMove")
    public Mono<GameResponse> swapMove(@PathVariable UUID gameID,
                                                 @PathVariable UUID playerID,
                                                 @RequestBody PutRabbitCardAndSwapTwoRequestBody body){
        return playerService.rabbitSwapCard(gameID,playerID,body).map(AppUtils::gameDtoToResponse);
    }



}
