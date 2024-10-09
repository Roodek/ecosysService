package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DragonflyCardTest extends CardTest {

    @Test
    void testDragonflyCount(){
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
        Card dragonfly1 = new DragonflyCard();
        Card dragonfly2 = new DragonflyCard();
        Card dragonfly3 = new DragonflyCard();

        FieldCard river1 = new RiverCard();
        FieldCard river2 = new RiverCard();
        FieldCard river3 = new RiverCard();
        FieldCard river4 = new RiverCard();
        FieldCard river5 = new RiverCard();
        FieldCard river6 = new RiverCard();
        FieldCard river7 = new RiverCard();


        var exampleBoard = new ArrayList<ArrayList<Card>>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(river1,fox1,river4 ,bee1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(river2,dragonfly1,river5,dragonfly2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(river3,dragonfly3 ,river6 ,bee2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(fox1  ,river7 ,bear3 ,rabbit2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(rabbit1,fish2 ,rabbit3  ,bear1)));

        setBoard(exampleBoard);
        assignNeighbours(board);

        assertEquals(6,dragonfly1.count());
        assertEquals(3,dragonfly2.count());
        assertEquals(7,dragonfly3.count());
    }
}
