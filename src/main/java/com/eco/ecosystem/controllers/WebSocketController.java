package com.eco.ecosystem.controllers;

import com.eco.ecosystem.controllers.responseObjects.BasicGameResponse;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.dto.PlayerDto;
import com.eco.ecosystem.entities.Message;
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


    @MessageMapping("/games") // Listens for messages sent to "/app/message"
    @SendTo("/topic/games")  // Sends responses to "/topic/messages" for subscribers
    public Message receiveMessage(Message message) {
        // Process and return the message (here we just return the received message)
        return message;
    }

    @MessageMapping("/games/{id}") // Listens for messages sent to "/app/message/{id}"
    @SendTo("/topic/games/{id}")  // Sends responses to "/topic/messages/{id}" for subscribers
    public Message receiveTargetedMessage(@DestinationVariable String id, @Payload Message message) {
        // Process and return the message (here we just return the received message)
        message.setContent(id+" : " +message.getContent());
        return message;
    }


}
