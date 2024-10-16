package com.eco.ecosystem.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong Card type")
public class InvalidCardTypeException extends Exception{

    public InvalidCardTypeException(){
        super("Invalid card type");
    }
}
