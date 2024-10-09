package com.eco.ecosystem.game.board;

public interface ProcessSlotStrategy {
    void processSlotAndWithItsNeighbours(Slot targetSlot, NeighborSlots neighbours);
}
