package com.eco.ecosystem.entities;

import com.eco.ecosystem.game.GamePlayer;
import com.eco.ecosystem.game.board.Board;
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

    public static final String ID_FIELD ="_id";
    public static final String CARDS_IN_HAND_FIELD ="cardsInHand";
    public static final String BOARD_FIELD ="board";
    public static final String SELECTED_MOVE_FIELD ="selectedMove";
    public static final String POINT_COUNT_FIELD ="pointCount";
    @JsonProperty("_id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("cardsInHand")
    private List<PlayerCard> cardsInHand;
    @JsonProperty("selectedMove")
    private SelectedMove selectedMove;
    @JsonProperty("board")
    private List<List<PlayerCard>> board;
    @JsonProperty("pointCount")
    private int pointCount;
    @JsonProperty("finalGeneralPointCount")
    private FinalGeneralCount finalGeneralPointCount;


    public GamePlayer toGamePlayer(){
        return new GamePlayer(name,new Board(board));
    }

    public Player(UUID id,
                  String name,
                  List<PlayerCard> cardsInHand,
                  SelectedMove selectedMove,
                  List<List<PlayerCard>> board,
                  int pointCount) {
        this.id = id;
        this.name = name;
        this.cardsInHand = cardsInHand;
        this.selectedMove = selectedMove;
        this.board = board;
        this.pointCount = pointCount;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cardsInHand=" + cardsInHand +
                ", selectedMove=" + selectedMove +
                ", board=" + board +
                ", pointCount=" + pointCount +
                ", finalGeneralPointCount=" + finalGeneralPointCount +
                '}';
    }

    public void printBoard(){
        new Board(board).printBoard();
    }
}
