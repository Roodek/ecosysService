package com.eco.ecosystem.game.board;

import com.eco.ecosystem.game.utils.BoardSlotProcessor;

import java.util.Set;

public class BoardAvailableMoveCalculator {

    private final Board board;
    private final CheckSlotsAvailabilityStrategy availableMovesProcessor;

    public BoardAvailableMoveCalculator(Board board) {
        this.board = board;
        this.availableMovesProcessor = new CheckSlotsAvailabilityStrategy(board);
    }

    public Set<Slot> getAvailableMoves() {
        if (board.getSizeHorizontal() == 0 || board.getSizeVertical() == 0) {
            return Set.of(new Slot(0, 0));
        } else {
            BoardSlotProcessor.iterateOverBoardEntriesAndApplyStrategy(board.getCardBoard(), availableMovesProcessor);
            return availableMovesProcessor.getAvailableMoves();
        }
    }

}
