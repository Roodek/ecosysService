package com.eco.ecosystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "games")
public class Game {

    @Id
    private UUID id;
    private List<Player> players;
    private List<PlayerCard> cardStack;

}

