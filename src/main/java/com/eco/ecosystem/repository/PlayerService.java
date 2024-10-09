package com.eco.ecosystem.repository;

import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    MongoTemplate mongoTemplate;

    public void updatePLayer(UUID gameID, UUID playerID, Player player) {
        Query query = new Query(
                Criteria.where(Game.ID_FIELD).is(gameID)
                        .and(Game.PLAYERS_FIELD + "." + Player.ID_FIELD).is(playerID.toString()));
        Update update = new Update().set(Game.PLAYERS_FIELD+".$",player);

        mongoTemplate.updateFirst(query,update,Game.class);
    }
}
