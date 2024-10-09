package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RiverCardTest extends CardTest {

    Card bear1 = new BearCard();
    Card bear2 = new BearCard();
    Card bear3 = new BearCard();
    Card bear4 = new BearCard();

    Card fox1 = new FoxCard();
    Card fish1 = new FishCard();
    Card fish2 = new FishCard();
    Card bee1 = new BeeCard();
    Card bee2 = new BeeCard();
    Card rabbit1 = new RabbitCard();
    Card rabbit2 = new RabbitCard();
    Card rabbit3 = new RabbitCard();
    Card rabbit4 = new RabbitCard();

    FieldCard river1 = new RiverCard();
    FieldCard river2 = new RiverCard();
    FieldCard river3 = new RiverCard();
    FieldCard river4 = new RiverCard();
    FieldCard river5 = new RiverCard();
    FieldCard river6 = new RiverCard();
    FieldCard river7 = new RiverCard();
    FieldCard river8 = new RiverCard();
    @Test
    void checkBuildingSingleEndedRiver(){
        var expectedBoard =new ArrayList<ArrayList<Card>>();

        expectedBoard.add(new ArrayList<>(Arrays.asList(river1,river2,fish1 ,river6)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bee1  ,river3,river4,river5)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1  ,bear2 ,fish2 ,bee2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bear4  ,bear1 ,bear3 ,river7)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(rabbit1  ,rabbit2  ,rabbit3  ,rabbit4)));

        setBoard(expectedBoard);
        assignNeighbours(board);//although we should assign neighbours manually, tests should be unitary and limited to test given class logic :P

        assertEquals(6,river1.getFieldSize());
        assertEquals(6,river2.getFieldSize());
        assertEquals(6,river4.getFieldSize());
        assertEquals(6,river5.getFieldSize());
        assertEquals(6,river6.getFieldSize());
        assertEquals(6,river3.getFieldSize());
        assertEquals(1,river7.getFieldSize());
    }

    @Test
    void checkBuildingDoubleEndedRiver(){
        var expectedBoard =new ArrayList<ArrayList<Card>>();

        expectedBoard.add(new ArrayList<>(Arrays.asList(bear2,river2,fish1 ,bear1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bee1  ,river3,river4,river5)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1  ,river1 ,fish2 ,bee2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bear4  ,river6 ,bear3 ,river7)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(rabbit1  ,rabbit2  ,rabbit3  ,rabbit4)));

        setBoard(expectedBoard);
        assignNeighbours(board);
        assertEquals(6,river2.getFieldSize());
        assertEquals(6,river3.getFieldSize());
        assertEquals(6,river1.getFieldSize());
        assertEquals(6,river4.getFieldSize());
        assertEquals(6,river5.getFieldSize());
        assertEquals(6,river6.getFieldSize());
        assertEquals(1,river7.getFieldSize());


    }

    @Test
    void checkBuildingCrossRiver(){
        var expectedBoard =new ArrayList<ArrayList<Card>>();

        expectedBoard.add(new ArrayList<>(Arrays.asList(bear2,river4,fish1 ,bear1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bee1  ,river3,fish2,bee2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(river1  ,river2 ,river7 ,river8)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bear4  ,river5 ,bear3 ,rabbit2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(rabbit1  ,river6  ,rabbit3  ,rabbit4)));

        setBoard(expectedBoard);
        assignNeighbours(board);

        assertEquals(8,river3.getFieldSize());
        assertEquals(8,river1.getFieldSize());
        assertEquals(8,river4.getFieldSize());
        assertEquals(8,river2.getFieldSize());
        assertEquals(8,river5.getFieldSize());
        assertEquals(8,river6.getFieldSize());
        assertEquals(8,river7.getFieldSize());
        assertEquals(8,river8.getFieldSize());

    }

    @Test
    void checkBuildingUShapeRiver(){
        var expectedBoard =new ArrayList<ArrayList<Card>>();

        var river9 = new RiverCard();

        expectedBoard.add(new ArrayList<>(Arrays.asList(river1,rabbit3,river9 ,bear1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(river2  ,fish1,river8,bee2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(river3  ,bee1 ,river7 ,fox1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(river4  ,river5 ,river6 ,rabbit2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bee1  ,rabbit1  ,bee2  ,rabbit4)));

        setBoard(expectedBoard);
        assignNeighbours(board);

        assertEquals(9,river3.getFieldSize());
        assertEquals(9,river1.getFieldSize());
        assertEquals(9,river4.getFieldSize());
        assertEquals(9,river2.getFieldSize());
        assertEquals(9,river5.getFieldSize());
        assertEquals(9,river6.getFieldSize());
        assertEquals(9,river7.getFieldSize());
        assertEquals(9,river8.getFieldSize());
        assertEquals(9,river9.getFieldSize());
    }

    @Test
    void checkBuildingPondShapeRiver(){
        var expectedBoard =new ArrayList<ArrayList<Card>>();

        var river9 = new RiverCard();

        expectedBoard.add(new ArrayList<>(Arrays.asList(river1,rabbit3,river9 ,bear1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(river2  ,fish1,river8,bee2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(river3  ,river6 ,fox1 ,fox1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(river4  ,river5 ,bee1 ,rabbit2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(bee1  ,rabbit1  ,bee2  ,rabbit4)));

        setBoard(expectedBoard);
        assignNeighbours(board);

        assertEquals(6,river3.getFieldSize());
        assertEquals(6,river1.getFieldSize());
        assertEquals(6,river4.getFieldSize());
        assertEquals(6,river2.getFieldSize());
        assertEquals(6,river5.getFieldSize());
        assertEquals(6,river6.getFieldSize());
        assertEquals(2,river8.getFieldSize());
        assertEquals(2,river9.getFieldSize());
    }
}
