package game.board;

import java.util.HashSet;
import java.util.Set;

public class CheckSlotsAvailabilityStrategy implements ProcessSlotStrategy{

    public CheckSlotsAvailabilityStrategy(Board board) {
        this.board = board;
    }

    private Board board;

    public Set<Slot> getAvailableMoves() {
        return availableMoves;
    }

    private Set<Slot> availableMoves = new HashSet<>();
    @Override
    public void processSlotAndWithItsNeighbours(Slot targetSlot, NeighborSlots neighbours) {
        if(board.getCardAtSlot(targetSlot)!=null){
            for(var slot:neighbours.toList()){
                if (board.getCardAtSlot(slot)==null){
                    availableMoves.add(slot);
                }
            }
        }
    }


}
