package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoxCardTest extends CardTest {

    @Test
    void testFoxCardCount(){

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

        FieldCard river4 = new RiverCard();
        FieldCard river6 = new RiverCard();
        FieldCard meadow1 = new MeadowCard();
        FieldCard meadow2 = new MeadowCard();
        FieldCard meadow5 = new MeadowCard();
        FieldCard meadow6 = new MeadowCard();

        var exampleBoard = new ArrayList<ArrayList<Card>>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(eagle1,fox2,fish2 ,fox3)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(meadow6  ,rabbit1,river4,wolf1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(fox1  ,wolf1 ,river6 ,meadow2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(bear1  ,meadow5 ,fish4 ,meadow1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(rabbit3  ,eagle2 ,rabbit4  ,fish3)));

        setBoard(exampleBoard);
        assignNeighbours(board);

        assertEquals(0,fox1.count());
        assertEquals(3,fox2.count());
        assertEquals(0,fox3.count());
    }
}
