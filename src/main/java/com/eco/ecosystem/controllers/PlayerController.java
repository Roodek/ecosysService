package com.eco.ecosystem.controllers;


import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.repository.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @PostMapping(path = "/{gameID}/{playerID}")
    public void updatePlayer(@PathVariable String gameID,
                             @PathVariable String playerID,
                             @RequestBody Player body){

        playerService.updatePLayer(UUID.fromString(gameID),UUID.fromString(playerID),body);
    }
}
