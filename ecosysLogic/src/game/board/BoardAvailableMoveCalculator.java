package game.board;

import game.utils.BoardSlotProcessor;

import java.util.Set;

public class BoardAvailableMoveCalculator {

    private final Board board;
    private final CheckSlotsAvailabilityStrategy availableMovesProcessor;

    public BoardAvailableMoveCalculator(Board board) {
        this.board = board;
        this.availableMovesProcessor = new CheckSlotsAvailabilityStrategy(board);
    }

    public Set<Slot> getAvailableMoves() {
        BoardSlotProcessor.iterateOverBoardEntriesAndApplyStrategy(board.getCardBoard(),availableMovesProcessor);
        return availableMovesProcessor.getAvailableMoves();
    }

}
