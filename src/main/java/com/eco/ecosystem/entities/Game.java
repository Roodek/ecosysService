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

    public static final String ID_FIELD = "_id";
    public static final String PLAYERS_FIELD = "players";
    public static final String CARD_STACK_FIELD = "cardStack";
    @Id
    private UUID id;
    private List<Player> players;
    private List<PlayerCard> cardStack;

    public enum SwapDirection {
        LEFT,
        RIGHT
    }
    public Game swapPlayersHands(SwapDirection direction) {
        if (direction == SwapDirection.LEFT) {
            List<PlayerCard> swapped = players.get(0).getCardsInHand();
            List<PlayerCard> buffer;
            for (int i = players.size() - 1; i >= 0; i--) {
                buffer = players.get(i).getCardsInHand();
                players.get(i).setCardsInHand(swapped);
                swapped = buffer;
            }
        } else {
            List<PlayerCard> swapped = players.get(players.size() - 1).getCardsInHand();
            List<PlayerCard> buffer;
            for (int i = 0; i < players.size(); i++) {
                buffer = players.get(i).getCardsInHand();
                players.get(i).setCardsInHand(swapped);
                swapped = buffer;
            }
        }
        return this;
    }

}

