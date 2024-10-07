package com.eco.ecosystem.services;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.AppUtils;

import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    // Get all products
    public Flux<GameDto> getProducts(){
        return gameRepository.findAll().map(AppUtils::entityToDto);
    }

    // Get product using Id
    public Mono<GameDto> getProduct(UUID id){
        return gameRepository.findById(id).map(AppUtils::entityToDto);
    }

    // Create Product
    public Mono<GameDto> saveProduct(Mono<GameDto> productDtoMono){
        return  productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(gameRepository::insert)
                .map(AppUtils::entityToDto);
    }

    // Update Product
    public Mono<GameDto> updateProduct(Mono<GameDto> productDtoMono, UUID id){
        return gameRepository.findById(id)
                .flatMap(p -> productDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(gameRepository::save)
                .map(AppUtils::entityToDto);
    }

    // Delete Product
    public Mono<Void> deleteProduct(UUID id){
        return gameRepository.deleteById(id);
    }
}
