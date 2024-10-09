package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeadowCardTest extends CardTest {


    @Test
    void testBasicMeadow(){
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

        FieldCard river1 = new RiverCard();
        FieldCard river2 = new RiverCard();
        FieldCard river3 = new RiverCard();
        FieldCard river4 = new RiverCard();
        FieldCard river5 = new RiverCard();
        FieldCard river6 = new RiverCard();
        FieldCard meadow1 = new MeadowCard();
        FieldCard meadow2 = new MeadowCard();
        FieldCard meadow3 = new MeadowCard();
        FieldCard meadow4 = new MeadowCard();
        FieldCard meadow5 = new MeadowCard();
        FieldCard meadow6 = new MeadowCard();

        var exampleBoard = new ArrayList<ArrayList<Card>>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(river1,river2,fish1 ,bee1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(meadow6  ,river3,river4,river5)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(fox1  ,bear2 ,river6 ,bee2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(bear4  ,meadow5 ,bear3 ,meadow1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(rabbit1  ,meadow4 ,meadow3  ,meadow2)));

        setBoard(exampleBoard);
        assignNeighbours(board);

        assertEquals(5,meadow1.getFieldSize());
        assertEquals(5, meadow2.getFieldSize());
        assertEquals(5, meadow3.getFieldSize());
        assertEquals(5, meadow4.getFieldSize());
        assertEquals(5, meadow5.getFieldSize());
        assertEquals(1,meadow6.getFieldSize());

        assertEquals(15,meadow5.count());
        //we set meadow5.calculated to true, so now it should return 0 just as its field members
        assertEquals(0,meadow5.count());
        assertEquals(0,meadow4.count());
        assertEquals(0,meadow3.count());
        assertEquals(0,meadow2.count());
        assertEquals(0,meadow1.count());
        assertEquals(0,meadow6.count());

    }


}
