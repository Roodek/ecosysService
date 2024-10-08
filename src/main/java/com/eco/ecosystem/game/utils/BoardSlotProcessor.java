package com.eco.ecosystem.game.utils;

import com.eco.ecosystem.game.board.NeighborSlots;
import com.eco.ecosystem.game.board.ProcessSlotStrategy;
import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.Card;

import java.util.List;

public class BoardSlotProcessor {

    public static void iterateOverBoardEntriesAndApplyStrategy(List<List<Card>> board, ProcessSlotStrategy strategy) {
        if(!board.isEmpty()) {
            for(int x =0;x<board.size();x++){
                for(int y = 0;y<board.get(x).size();y++){
                    strategy.processSlotAndWithItsNeighbours(new Slot(x,y),getNeighbours(board, x, y));
                }
            }
        }
    }

    private static NeighborSlots getNeighbours(List<List<Card>> board, int x, int y){
        var neighbours = new NeighborSlots();
        if(isLeftUpperCorner(board, x, y)){
            addRightSlot(x, y, neighbours);
            addBottomSlot(x, y, neighbours);
        }
        else if(isUpperRightCorner(board, x, y)){
            addLeftSlot(x,y,neighbours);
            addBottomSlot(x,y,neighbours);
        }
        else if(isBottomLeftCorner(board, x, y)){
            addTopSlot(x,y,neighbours);
            addRightSlot(x,y,neighbours);
        }
        else if(isBottomRightCorner(board, x, y)){
            addTopSlot(x,y,neighbours);
            addLeftSlot(x,y,neighbours);
        }
        else if(isLeftWall(board, x, y)){
            addTopSlot(x,y,neighbours);
            addBottomSlot(x,y,neighbours);
            addRightSlot(x,y,neighbours);
        }
        else if(isRightWall(board,x,y)){
            addTopSlot(x,y,neighbours);
            addBottomSlot(x,y,neighbours);
            addLeftSlot(x,y,neighbours);
        }
        else if(isUpperWall(board,x,y)){
            addRightSlot(x,y,neighbours);
            addBottomSlot(x,y,neighbours);
            addLeftSlot(x,y,neighbours);
        }
        else if(isBottomWall(board,x,y)){
            addRightSlot(x,y,neighbours);
            addTopSlot(x,y,neighbours);
            addLeftSlot(x,y,neighbours);
        }
        else {
            addRightSlot(x,y,neighbours);
            addTopSlot(x,y,neighbours);
            addLeftSlot(x,y,neighbours);
            addBottomSlot(x,y,neighbours);
        }
        return neighbours;
    }

    private static boolean isBottomWall(List<List<Card>> board, int x, int y) {
        return x == board.size()-1 && y < board.get(x).size() - 1 && y > 0;
    }
    private static boolean isUpperWall(List<List<Card>> board, int x, int y) {
        return x == 0 && y < board.get(x).size() - 1 && y > 0;
    }
    private static boolean isLeftWall(List<List<Card>> board, int x, int y) {
        return x > 0 && x < board.size() - 1 && y == 0;
    }
    private static boolean isRightWall(List<List<Card>> board, int x, int y) {
        return x > 0 && x < board.size() - 1 && y == board.get(x).size()-1;
    }

    private static boolean isBottomRightCorner(List<List<Card>> board, int x, int y) {
        return x == board.size() - 1 && y == board.get(x).size() - 1;
    }

    private static boolean isBottomLeftCorner(List<List<Card>> board, int x, int y) {
        return x == board.size() - 1 && y == 0;
    }

    private static boolean isUpperRightCorner(List<List<Card>> board, int x, int y) {
        return x == 0 && y == board.get(x).size() - 1;
    }

    private static boolean isLeftUpperCorner(List<List<Card>> board, int x, int y) {
        return x == 0 && y == 0;
    }

    private static void addBottomSlot(int x, int y, NeighborSlots availableSlots) {
            availableSlots.setBottomNeighbour(new Slot(x+1, y ));
    }

    private static void addRightSlot(int x, int y, NeighborSlots availableSlots) {
            availableSlots.setRightNeighbour(new Slot(x, y+1));
    }
    private static void addTopSlot(int x, int y, NeighborSlots availableSlots) {
            availableSlots.setTopNeighbour(new Slot(x-1, y));
    }
    private static void addLeftSlot(int x, int y, NeighborSlots availableSlots) {
            availableSlots.setLeftNeighbour(new Slot(x, y-1));
    }
}
