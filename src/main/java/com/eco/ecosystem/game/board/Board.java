package com.eco.ecosystem.game.board;

import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.cards.Card;
import com.eco.ecosystem.game.cards.FieldCard;
import com.eco.ecosystem.game.exceptions.InvalidCardTypeException;
import com.eco.ecosystem.game.exceptions.InvalidMoveException;
import com.eco.ecosystem.game.utils.BoardSlotProcessor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Board {
    public static final int CARD_NAME_LENGTH = 11;
    private int sizeVertical = 0;
    private int sizeHorizontal = 0;
    private int maxVerticalSize = 5;
    private int maxHorizontalSize = 5;
    private int maxRiverLength = 0;
    private int wolfCount = 0;
    private List<List<Card>> cardBoard = new ArrayList<>();

    public Board() {
    }

    public List<List<PlayerCard>> toResponseBoard() {
        return cardBoard.stream().map(row -> row.stream().map(elem -> elem == null ? null : new PlayerCard(elem.getType().toString())).toList()).toList();
    }

    public Board(List<List<PlayerCard>> playersCardBoard) {
        this.cardBoard = new ArrayList<>(playersCardBoard.stream()
                .map(row -> new ArrayList<>(row.stream()
                        .map(playerCard -> {
                            try {
                                return playerCard==null?null:Card.fromString(playerCard.getCardType());
                            } catch (InvalidCardTypeException e) {
                                throw new RuntimeException(e);
                            }
                        }).toList())).toList());
        readVerticalAndHorizontalSizeFromDBCardBoard(playersCardBoard);
    }

    private void readVerticalAndHorizontalSizeFromDBCardBoard(List<List<PlayerCard>> playersCardBoard) {
        if(playersCardBoard.size()==1 && playersCardBoard.get(0).isEmpty()){
            return;
        }
        if (playersCardBoard.get(0).stream().allMatch(Objects::isNull)) {
            this.sizeVertical = playersCardBoard.size() - 2;
        } else {
            this.sizeVertical = playersCardBoard.size();
            if (this.sizeVertical == 5) {
                this.maxVerticalSize = 5;
                this.maxHorizontalSize = 4;
            }
        }
        if (playersCardBoard.stream().map(row -> row.get(0)).allMatch(Objects::isNull)) {
            this.sizeHorizontal = playersCardBoard.get(0).size() - 2;
        } else {
            this.sizeHorizontal = playersCardBoard.get(0).size();
            if (this.sizeHorizontal == 5) {
                this.maxHorizontalSize = 5;
                this.maxVerticalSize = 4;
            }
        }

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
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        System.out.println();
    }

    public Card getCardAtSlot(Slot slot) {
        if (slot == null) {
            return null;
        } else {
            return cardBoard.get(slot.coordX()).get(slot.coordY());
        }
    }

    public void putFirstCard(Card card) throws InvalidMoveException {
        putCard(card, new Slot(0,0));
    }

    public Board putRabbitCard(Slot rabbitSlot,Slot slotToSwap1,Slot slotToSwap2) throws InvalidMoveException {
        return slotToSwap1 !=null && slotToSwap2!=null ?
                putCardAndPerformAction(Card.from(Card.CardType.RABBIT),rabbitSlot,slotToSwap1,slotToSwap2):
                putCard(Card.from(Card.CardType.RABBIT),rabbitSlot);
    }
    public Board putCard(Card card, Slot slot) throws InvalidMoveException {
        return putCardAndPerformAction(card,slot,null,null);
    }

    private Board putCardAndPerformAction(Card card, Slot slot,Slot slotToSwap1,Slot slotToSwap2) throws IndexOutOfBoundsException, InvalidMoveException {
        if (isBoardCompleted()) {
            throw new InvalidMoveException("Invalid move, board is already completed");
        }
        if (cardBoard.isEmpty() || cardBoard.get(0).isEmpty()) {
            initBoard();
            cardBoard.get(1).set(1, card);
            sizeVertical++;
            sizeHorizontal++;
        } else {
            if (cardBoard.get(slot.coordX()).get(slot.coordY()) != null) {
                throw new InvalidMoveException("Invalid move, slot already taken");
            }
            cardBoard.get(slot.coordX()).set(slot.coordY(), card);
            if(card.getType() == Card.CardType.RABBIT && slotToSwap1!=null && slotToSwap2!=null ){
               rabbitSwap(slotToSwap1,slotToSwap2);
            }
            handlePossibleBoardSizeChange(slot);
        }
        if (isBoardCompleted()) {
            establishCardRelationships();
        }
        return this;
    }

    private void handlePossibleBoardSizeChange(Slot slot) {
        if (slot.coordX() == 0 && sizeVertical < maxVerticalSize) {
            addNewFirstRow();
            sizeVertical++;
        } else if (slot.coordX() == sizeVertical + 1 && sizeVertical < maxVerticalSize) {
            addNewLastRow();
            sizeVertical++;
        } else if (slot.coordY() == 0 && sizeHorizontal < maxHorizontalSize) {
            addNewFirstColumn();
            sizeHorizontal++;
        } else if (slot.coordY() == sizeHorizontal + 1 && sizeHorizontal < maxHorizontalSize) {
            addNewLastColumn();
            sizeHorizontal++;
        }
        checkIfMaxDimensionsAndCutBoardToAllowedDimension();
    }

    private void rabbitSwap(Slot card1, Slot card2) {
        Card cardAtFirstSlot = getCardAtSlot(card1);
        cardBoard.get(card1.coordX()).set(card1.coordY(), getCardAtSlot(card2));
        cardBoard.get(card2.coordX()).set(card2.coordY(), cardAtFirstSlot);
    }

    private boolean isBoardCompleted() {
        return cardBoard.stream()
                .flatMap(Collection::stream).filter(Objects::nonNull).count() == 20;
    }
    private void checkIfMaxDimensionsAndCutBoardToAllowedDimension() {
        checkIfMaxHorizontalAndCutBoardTo4x5();
        checkIfMaxVerticalAndCutBoardTo5x4();
    }

    private void checkIfMaxVerticalAndCutBoardTo5x4() {
        if (sizeVertical == maxVerticalSize && cardBoard.get(0).stream().allMatch(Objects::isNull)) {
            cardBoard = new ArrayList<>(cardBoard.subList(1, maxVerticalSize + 1));
            maxHorizontalSize = 4;
            checkIfMaxHorizontalAndCutBoardTo4x5();
        }
    }

    private void checkIfMaxHorizontalAndCutBoardTo4x5() {
        if (sizeHorizontal == maxHorizontalSize && cardBoard.stream().map(row->row.get(0)).allMatch(Objects::isNull)) {
            cardBoard.replaceAll(cards -> new ArrayList<>(cards.subList(1, maxHorizontalSize + 1)));
            maxVerticalSize = 4;
            checkIfMaxVerticalAndCutBoardTo5x4();
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
        List<List<Card>> newBoard = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            newBoard.add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                newBoard.get(i).add(null);
            }
        }
        this.cardBoard = newBoard;
    }

    public void establishCardRelationships() {
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
