package com.eco.ecosystem.repository;

import com.eco.ecosystem.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, UUID> {
}
