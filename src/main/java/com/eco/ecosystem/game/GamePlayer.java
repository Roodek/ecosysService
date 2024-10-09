package com.eco.ecosystem.game;



import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.cards.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public class GamePlayer {

    private List<Card> hand;
    private String name;
    private Board board;
    private Integer numberOfGaps = 0;
    private Integer sumOfPoints = 0;
    private Integer ecosystemGapPoints = 0;

    private Map<Card.CardType, Integer> generalPointCount = new HashMap<>();

    public GamePlayer(String name, Board board){
        this.name = name;
        this.board = board;
        Arrays.stream(Card.CardType.values()).forEach(cardType -> generalPointCount.put(cardType,0));
    }

}
