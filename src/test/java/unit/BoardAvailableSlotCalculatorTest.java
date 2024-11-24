package unit;

import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.board.BoardAvailableMoveCalculator;
import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BoardAvailableSlotCalculatorTest {
    private Board board;
    private Card wolf = new WolfCard();
    private Card river = new RiverCard();
    private Card bee = new BeeCard();
    private Card elk = new ElkCard();
    private Card fox = new FoxCard();
    private Card meadow = new MeadowCard();
    private Card dragonfly = new DragonflyCard();
    private Card fish= new FishCard();
    private Card rabbit= new RabbitCard();
    private Card eagle= new EagleCard();
    private Card bear= new BearCard();

    private BoardAvailableMoveCalculator calculator;

    @AfterEach
    void finalizeWith(){
        board.printBoard();
        System.out.println();
    }

    @Test
    void checkAvailableSlotsInBoard2x2(){
        board = new Board();
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,eagle,fish,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,wolf,bee,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
        board.setSizeVertical(2);
        board.setSizeHorizontal(2);
        calculator = new BoardAvailableMoveCalculator(board);
        var expectedAvailableMoves = Set.of(
                new Slot(0,2),
                new Slot(0,3),
                new Slot(1,1),
                new Slot(1,4),
                new Slot(2,0),
                new Slot(2,3),
                new Slot(3,1),
                new Slot(3,2)
                );

        assertEquals(expectedAvailableMoves,calculator.getAvailableMoves());
    }

    @Test
    void checkAvailableSlotsInBoard5x3(){
        board = new Board();
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,elk,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,eagle,fish,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,wolf,bee,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,rabbit,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,dragonfly,null,null)));
        board.setSizeVertical(5);
        board.setSizeHorizontal(3);
        calculator = new BoardAvailableMoveCalculator(board);
        var expectedAvailableMoves = Set.of(
                new Slot(0,1),
                new Slot(0,3),
                new Slot(1,1),
                new Slot(1,4),
                new Slot(2,0),
                new Slot(2,3),
                new Slot(3,1),
                new Slot(3,3),
                new Slot(4,1),
                new Slot(4,3)
        );

        var availableMoves = calculator.getAvailableMoves();
        assertEquals(expectedAvailableMoves.size(),availableMoves.size());
        assertEquals(expectedAvailableMoves,availableMoves);
    }

    @Test
    void checkAvailableSlotsInBoard3x5(){
        board = new Board();
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,wolf,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(elk,eagle,bee,fish,dragonfly)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,rabbit,null,null)));
        board.getCardBoard().add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
        calculator = new BoardAvailableMoveCalculator(board);
        board.setSizeVertical(3);
        board.setSizeHorizontal(5);
        var expectedAvailableMoves = Set.of(
                new Slot(0,2),
                new Slot(1,0),
                new Slot(1,1),
                new Slot(1,3),
                new Slot(1,4),
                new Slot(3,0),
                new Slot(3,1),
                new Slot(3,3),
                new Slot(3,4),
                new Slot(4,2)
        );

        var availableMoves = calculator.getAvailableMoves();
        assertEquals(expectedAvailableMoves.size(),availableMoves.size());
        assertEquals(expectedAvailableMoves,availableMoves);
    }

}
