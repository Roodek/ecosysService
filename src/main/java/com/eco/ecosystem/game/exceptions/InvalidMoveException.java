package com.eco.ecosystem.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Move not allowed")
public class InvalidMoveException extends Exception{
    public InvalidMoveException(String message){
        super("Invalid move: "+message);
    }
}
