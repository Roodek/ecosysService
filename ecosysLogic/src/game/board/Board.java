package game.board;

import game.cards.Card;
import game.cards.ElkCard;
import game.cards.FieldCard;
import game.exceptions.InvalidMoveException;
import game.utils.BoardSlotProcessor;

import java.util.*;

public class Board {
    public static final int CARD_NAME_LENGTH = 11;
    private int sizeVertical = 0;
    private int sizeHorizontal = 0;
    private int maxVerticalSize = 5;
    private int maxHorizontalSize = 5;
    private int maxRiverLength = 0;
    private int wolfCount = 0;
    private ArrayList<ArrayList<Card>> cardBoard = new ArrayList<>();

    public int getMaxRiverLength() {
        return maxRiverLength;
    }

    public void setMaxRiverLength(int maxRiverLength) {
        this.maxRiverLength = maxRiverLength;
    }

    public int getWolfCount() {
        return wolfCount;
    }

    public void setWolfCount(int wolfCount) {
        this.wolfCount = wolfCount;
    }

    public Board() {}

    public void setSizeVertical(int sizeVertical) {
        this.sizeVertical = sizeVertical;
    }

    public void setSizeHorizontal(int sizeHorizontal) {
        this.sizeHorizontal = sizeHorizontal;
    }

    public int getSizeVertical() {
        return sizeVertical;
    }

    public int getSizeHorizontal() {
        return sizeHorizontal;
    }

    public void printBoard() {
        for (var row : cardBoard) {
            for (var card : row) {
                if (card != null) {
                    var padding = CARD_NAME_LENGTH - (card.getType().toString().length() + 1);
                    System.out.printf(" %s%s", card.getType().toString(), " ".repeat(Math.max(0, padding)));
                } else {
                    System.out.printf("[        ]%s", " ".repeat(Math.max(0, CARD_NAME_LENGTH - 10)));
                }
            }
            System.out.println();
        }

    }

    public Card getCardAtSlot(Slot slot) {
        if (slot == null) {
            return null;
        } else {
            return cardBoard.get(slot.coordX()).get(slot.coordY());
        }
    }

    public ArrayList<ArrayList<Card>> getCardBoard() {
        return cardBoard;
    }

    public void putFirstCard(Card card) throws InvalidMoveException {
        putCard(card, 0, 0);
    }

    public void putCard(Card card, int coordX, int coordY) throws IndexOutOfBoundsException, InvalidMoveException {
        if (isBoardCompleted()) {
            throw new InvalidMoveException("Invalid move, board is already completed");
        }
        if (cardBoard.isEmpty()) {
            initBoard();
            cardBoard.get(1).set(1, card);
            sizeVertical++;
            sizeHorizontal++;
        } else {
            if (cardBoard.get(coordX).get(coordY) != null) {
                throw new InvalidMoveException("Invalid move, slot already taken");
            }
            cardBoard.get(coordX).set(coordY, card);
            if (coordX == 0 && sizeVertical < 5) {
                addNewFirstRow();
                sizeVertical++;
                checkIfMaxVerticalAndCutBoardTo5x4();
            } else if (coordX == sizeVertical + 1 && sizeVertical < 5) {
                addNewLastRow();
                sizeVertical++;
                checkIfMaxVerticalAndCutBoardTo5x4();
            } else if (coordY == 0 && sizeHorizontal < 5) {
                addNewFirstColumn();
                sizeHorizontal++;
                checkIfMaxHorizontalAndCutBoardTo4x5();
            } else if (coordY == sizeHorizontal + 1 && sizeHorizontal < 5) {
                addNewLastColumn();
                sizeHorizontal++;
                checkIfMaxHorizontalAndCutBoardTo4x5();
            }
        }
        if (isBoardCompleted()) {
            assignNeighbours();
            mergeRiversAndMeadows();
        }

    }

    public void rabbitSwap(Slot card1,Slot card2){
        Card cardAtFirstSlot = getCardAtSlot(card1);
        cardBoard.get(card1.coordX()).set(card1.coordY(),getCardAtSlot(card2));
        cardBoard.get(card2.coordX()).set(card2.coordY(),cardAtFirstSlot);
    }

    private boolean isBoardCompleted() {
        return cardBoard.stream()
                .flatMap(Collection::stream).filter(Objects::nonNull).count() == 20;
    }

    private void checkIfMaxVerticalAndCutBoardTo5x4() {
        if (sizeVertical == maxVerticalSize) {
            cardBoard = new ArrayList<>(cardBoard.subList(1, maxVerticalSize + 1));
            maxHorizontalSize = 4;
        }
    }

    private void checkIfMaxHorizontalAndCutBoardTo4x5() {
        if (sizeHorizontal == maxHorizontalSize) {
            cardBoard.replaceAll(cards -> new ArrayList<>(cards.subList(1, maxHorizontalSize + 1)));
            maxVerticalSize = 4;
        }
    }

    private void addNewLastColumn() {
        cardBoard.forEach(row -> row.add(null));
    }

    private void addNewFirstColumn() {
        cardBoard.forEach(row -> row.add(0, null));
    }

    private void addNewLastRow() {
        cardBoard.add(new ArrayList<>(Collections.nCopies(cardBoard.get(0).size(), null)));
    }

    private void addNewFirstRow() {
        cardBoard.add(0, new ArrayList<>(Collections.nCopies(cardBoard.get(0).size(), null)));
    }

    private void initBoard() {
        for (int i = 0; i < 3; i++) {
            cardBoard.add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                cardBoard.get(i).add(null);
            }
        }
    }

    private void assignNeighbours() {
        var assignNeighboursStrategy = new AssignNeighboursToCardsStrategy(this);
        BoardSlotProcessor.iterateOverBoardEntriesAndApplyStrategy(cardBoard, assignNeighboursStrategy);
        mergeRiversAndMeadows();

    }

    private void mergeRiversAndMeadows() {
        cardBoard.stream()
                .flatMap(Collection::stream)
                .filter(card -> card.getType() == Card.CardType.RIVER || card.getType() == Card.CardType.MEADOW)
                .map(card -> (FieldCard) card)
                .forEach(FieldCard::mergeFieldCards);

    }
}
