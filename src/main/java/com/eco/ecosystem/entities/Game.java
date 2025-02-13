package com.eco.ecosystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import utils.Timestamp;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "games")
public class Game {
    public static final String COLLECTION_NAME = "games";
    public static final String ID_FIELD = "_id";
    public static final String PLAYERS_FIELD = "players";
    public static final String TURN = "turn";
    public static final String CARD_STACK_FIELD = "cardStack";
    public static final String CREATED_AT_FIELD = "createdAt";
    @Id
    private UUID id;
    private List<Player> players;
    private List<PlayerCard> cardStack;
    private int turn;
    private Timestamp createdAt;

    public Game(UUID id, List<Player> players, List<PlayerCard> cardStack, int turn) {
        this.id = id;
        this.players = players;
        this.cardStack = cardStack;
        this.turn = turn;
        this.createdAt = Timestamp.now();
    }
}

