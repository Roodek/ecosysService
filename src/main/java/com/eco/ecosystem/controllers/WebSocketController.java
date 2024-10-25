package com.eco.ecosystem.controllers;

import com.eco.ecosystem.controllers.responseObjects.BasicGameResponse;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.dto.PlayerDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.services.GameService;
import com.eco.ecosystem.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @MessageMapping("/game/{gameID}/join")
    @SendTo("/topic/public")
    public Flux<BasicGameResponse> playerJoined(@DestinationVariable UUID gameID,
                                                @Payload Player player,
                                                SimpMessageHeaderAccessor headerAccessor) {
        if (player != null)
            headerAccessor.getSessionAttributes().put("player", player);
        return gameService.getAllGames().map(gameDto -> new BasicGameResponse(gameDto.getId(),gameDto.getPlayers().stream().map(Player::getId).toList()));

    }

    @MessageMapping("/game/{gameID}/updateBoards")
    public void updateBoards(@DestinationVariable UUID gameID) {
        gameService.swapPlayerHands(gameID);
        simpMessagingTemplate.convertAndSend("/specific/" + gameID.toString(),
                playerService.getPlayers(gameID)
                        .flatMap(players -> Mono.just(players.stream()
                                .map(player -> new PlayerDto(player.getId(), player.getBoard())).toList())));
    }

    @MessageMapping("/game.endGame")
    @SendTo("/topic/public")
    public Mono<GameDto> endGame(@Payload UUID gameID) {
        return gameService.endGame(gameID);
    }


}
