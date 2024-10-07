package com.eco.ecosystem.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private Long id;
    private String name;
    private List<PlayerCard> cardsInHand;
    private List<List<PlayerCard>> board;
    private int pointCount;

    public Player(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name+" - "+this.pointCount+ " - "+this.cardsInHand;
    }
}
