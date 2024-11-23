package com.eco.ecosystem.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalMoveException extends RuntimeException{
    public IllegalMoveException(String message){
        super(message);
    }
}
