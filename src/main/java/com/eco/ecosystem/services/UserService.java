package com.eco.ecosystem.services;

import com.eco.ecosystem.entities.User;
import com.eco.ecosystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //TODO add Dto
    public Mono<User> saveUser(Mono<User> productDtoMono){
        return  productDtoMono
                .flatMap(userRepository::insert);
    }


}
