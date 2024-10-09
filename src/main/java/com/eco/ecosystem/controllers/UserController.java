package com.eco.ecosystem.controllers;

import com.eco.ecosystem.entities.User;
import com.eco.ecosystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    @Autowired
    private UserService service;
    @PostMapping
    public Mono<User> saveUser(@RequestBody Mono<User> user){
        return service.saveUser(user);
    }
}
