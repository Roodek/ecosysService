package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FishCardTest extends CardTest {

    @Test
    void testFishCardCount(){
        Card bear1 = new BearCard();
        Card wolf1 = new WolfCard();

        Card fox1 = new FoxCard();
        Card fox2 = new FoxCard();
        Card fox3 = new FoxCard();
        Card fish2 = new FishCard();
        Card fish3 = new FishCard();
        Card fish4 = new FishCard();
        Card rabbit1 = new RabbitCard();
        Card rabbit3 = new RabbitCard();
        Card rabbit4 = new RabbitCard();
        Card eagle1 = new EagleCard();
        Card eagle2 = new EagleCard();

        Card dragonfly1 = new DragonflyCard();
        Card dragonfly2 = new DragonflyCard();
        Card dragonfly3 = new DragonflyCard();

        FieldCard river4 = new RiverCard();
        FieldCard river6 = new RiverCard();
        FieldCard meadow1 = new MeadowCard();


        var exampleBoard = new ArrayList<ArrayList<Card>>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(eagle1,fox2,rabbit1 ,fox3)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(dragonfly1,fish2,river4,wolf1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(fox1  ,river6 ,wolf1 ,dragonfly3)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(bear1  ,dragonfly2 ,fish4 ,meadow1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(rabbit3  ,eagle2 ,rabbit4  ,fish3)));

        setBoard(exampleBoard);
        assignNeighbours(board);

        assertEquals(6,fish2.count());
        assertEquals(0,fish3.count());
        assertEquals(2,fish4.count());
    }
}
