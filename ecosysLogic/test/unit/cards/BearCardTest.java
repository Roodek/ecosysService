package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BearCardTest extends CardTest {

    Card bear1 = new BearCard();
    Card bear2 = new BearCard();
    Card bear3 = new BearCard();
    Card bear4 = new BearCard();

    Card fox1 = new FoxCard();
    Card fish1 = new FishCard();
    Card fish2 = new FishCard();
    Card bee1 = new BeeCard();
    @Test
    void testCountPoints(){
        var expectedBoard =new ArrayList<ArrayList<Card>>();

        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1,fox1,fox1,fox1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1,bear1,bee1,fox1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1,bear2,fish1,fox1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1,fish2,bear3,fox1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1,fox1,fox1,fox1)));

        setBoard(expectedBoard);
        assignNeighbours(board);//although we should assign neighbours manually, tests should be unitary and limited to test given class logic :P

        assertEquals(2,bear1.count());
        assertEquals(4,bear2.count());
        assertEquals(4,bear3.count());
       /*
        *
        etc...
        *
        * */
    }
}
