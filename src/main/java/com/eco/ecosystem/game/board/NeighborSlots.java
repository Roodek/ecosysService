package com.eco.ecosystem.game.board;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Setter
@Getter
public class NeighborSlots {
    Slot topNeighbour;
    Slot bottomNeighbour;
    Slot leftNeighbour;
    Slot rightNeighbour;

    public List<Slot> toList(){
        return Stream.of(topNeighbour,bottomNeighbour,leftNeighbour,rightNeighbour).filter(Objects::nonNull).toList();
    }

}
