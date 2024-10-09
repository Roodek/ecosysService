package game.utils;

import game.board.NeighborSlots;
import game.board.Slot;
import game.board.ProcessSlotStrategy;
import game.cards.Card;

import java.util.ArrayList;

public class BoardSlotProcessor {

    public static void iterateOverBoardEntriesAndApplyStrategy(ArrayList<ArrayList<Card>> board, ProcessSlotStrategy strategy) {
        if(!board.isEmpty()) {
            for(int x =0;x<board.size();x++){
                for(int y = 0;y<board.get(x).size();y++){
                    strategy.processSlotAndWithItsNeighbours(new Slot(x,y),getNeighbours(board, x, y));
                }
            }
        }
    }

    private static NeighborSlots getNeighbours(ArrayList<ArrayList<Card>> board, int x, int y){
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

    private static boolean isBottomWall(ArrayList<ArrayList<Card>> board, int x, int y) {
        return x == board.size()-1 && y < board.get(x).size() - 1 && y > 0;
    }
    private static boolean isUpperWall(ArrayList<ArrayList<Card>> board, int x, int y) {
        return x == 0 && y < board.get(x).size() - 1 && y > 0;
    }
    private static boolean isLeftWall(ArrayList<ArrayList<Card>> board, int x, int y) {
        return x > 0 && x < board.size() - 1 && y == 0;
    }
    private static boolean isRightWall(ArrayList<ArrayList<Card>> board, int x, int y) {
        return x > 0 && x < board.size() - 1 && y == board.get(x).size()-1;
    }

    private static boolean isBottomRightCorner(ArrayList<ArrayList<Card>> board, int x, int y) {
        return x == board.size() - 1 && y == board.get(x).size() - 1;
    }

    private static boolean isBottomLeftCorner(ArrayList<ArrayList<Card>> board, int x, int y) {
        return x == board.size() - 1 && y == 0;
    }

    private static boolean isUpperRightCorner(ArrayList<ArrayList<Card>> board, int x, int y) {
        return x == 0 && y == board.get(x).size() - 1;
    }

    private static boolean isLeftUpperCorner(ArrayList<ArrayList<Card>> board, int x, int y) {
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
