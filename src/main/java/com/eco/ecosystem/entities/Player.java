package com.eco.ecosystem.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    public static final String ID_FIELD ="id";
    public static final String CARDS_IN_HAND_FIELD ="cardsInHand";
    public static final String BOARD_FIELD ="board";
    public static final String POINT_COUNT_FIELD ="pointCount";
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("cardsInHand")
    private List<PlayerCard> cardsInHand;
    @JsonProperty("board")
    private List<List<PlayerCard>> board;
    @JsonProperty("pointCount")
    private int pointCount;

    @Override
    public String toString() {
        return this.name+" - "+this.pointCount+ " - "+this.cardsInHand;
    }

}
