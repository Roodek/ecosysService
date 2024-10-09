package com.eco.ecosystem.services;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.CardStack;
import com.eco.ecosystem.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.AppUtils;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Flux<GameDto> getAllGames(){
        return gameRepository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<GameDto> getGame(UUID id){
        return gameRepository.findById(id).map(AppUtils::entityToDto);
    }

    public Mono<GameDto> saveGame(Mono<GameDto> productDtoMono){
        return  productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(gameRepository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<UUID> createGame(List<Player> players){
        var newId = UUID.randomUUID();
        var newGame =  Mono.just(new GameDto(newId, players, CardStack.initCardStack().stream().map(PlayerCard::new).toList()));
        return  newGame.map(AppUtils::dtoToEntity)
                .flatMap(gameRepository::insert)
                .then(Mono.just(newId));
    }

    public Mono<GameDto> updateGame(Mono<GameDto> gameDtoMono, UUID id){
        return gameRepository.findById(id)
                .flatMap(p -> gameDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(gameRepository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteGame(UUID id){
        return gameRepository.deleteById(id);
    }
}
