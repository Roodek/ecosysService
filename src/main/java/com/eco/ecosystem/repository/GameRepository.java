package com.eco.ecosystem.repository;


import com.eco.ecosystem.entities.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, UUID> {
}
