package game;

import game.board.Board;
import game.cards.Card;
import game.cards.ElkCard;
import game.cards.FieldCard;

import java.util.*;

public class Player {

    private List<Card> hand;
    private String name;
    private Board board;

    private Integer numberOfGaps = 0;
    private Integer sumOfPoints = 0;
    private Integer ecosystemGapPoints = 0;

    private Map<Card.CardType, Integer> generalPointCount = new HashMap<>();

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Player(String name, Board board){
        this.name = name;
        this.board = board;
        Arrays.stream(Card.CardType.values()).forEach(cardType -> generalPointCount.put(cardType,0));
    }
    public Map<Card.CardType, Integer> getGeneralPointCount() {
        return generalPointCount;
    }
    public Board getBoard() {
        return board;
    }

    public Integer getNumberOfGaps() {
        return numberOfGaps;
    }
    public String getName() {
        return this.name;
    }

    public void setNumberOfGaps(Integer numberOfGaps) {
        this.numberOfGaps = numberOfGaps;
    }

    public Integer getSumOfPoints() {
        return sumOfPoints;
    }

    public void setSumOfPoints(Integer sumOfPoints) {
        this.sumOfPoints = sumOfPoints;
    }

    public Integer getEcosystemGapPoints() {
        return ecosystemGapPoints;
    }

    public void setEcosystemGapPoints(Integer ecosystemGapPoints) {
        this.ecosystemGapPoints = ecosystemGapPoints;
    }

}
