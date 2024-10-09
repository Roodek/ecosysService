package game.board;


public class AssignNeighboursToCardsStrategy implements ProcessSlotStrategy{

    private Board board;

    public AssignNeighboursToCardsStrategy(Board board) {
        this.board = board;
    }

    @Override
    public void processSlotAndWithItsNeighbours(Slot targetSlot, NeighborSlots neighbours) {
        if(board.getCardAtSlot(targetSlot)!=null) {
            if (board.getCardAtSlot(neighbours.getTopNeighbour()) != null)
                board.getCardAtSlot(targetSlot).setUpperNeighbour(board.getCardAtSlot(neighbours.getTopNeighbour()));
            if (board.getCardAtSlot(neighbours.getBottomNeighbour()) != null)
                board.getCardAtSlot(targetSlot).setBottomNeighbour(board.getCardAtSlot(neighbours.getBottomNeighbour()));
            if (board.getCardAtSlot(neighbours.getLeftNeighbour()) != null)
                board.getCardAtSlot(targetSlot).setLeftNeighbour(board.getCardAtSlot(neighbours.getLeftNeighbour()));
            if (board.getCardAtSlot(neighbours.getRightNeighbour()) != null)
                board.getCardAtSlot(targetSlot).setRightNeighbour(board.getCardAtSlot(neighbours.getRightNeighbour()));
        }
    }
}
