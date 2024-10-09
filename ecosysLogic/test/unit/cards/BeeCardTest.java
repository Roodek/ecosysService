package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeeCardTest extends CardTest {

    @Test
    void testBeeCount(){
        Card bear1 = new BearCard();
        Card bear2 = new BearCard();

        Card fox1 = new FoxCard();
        Card fish1 = new FishCard();
        Card fish2 = new FishCard();
        Card bee1 = new BeeCard();
        Card bee2 = new BeeCard();
        Card bee3 = new BeeCard();
        Card rabbit1 = new RabbitCard();
        Card rabbit2 = new RabbitCard();
        Card rabbit3 = new RabbitCard();
        Card dragonfly1 = new DragonflyCard();
        Card dragonfly2 = new DragonflyCard();

        FieldCard meadow1 = new MeadowCard();
        FieldCard meadow2 = new MeadowCard();
        FieldCard meadow3 = new MeadowCard();
        FieldCard meadow4 = new MeadowCard();
        FieldCard meadow5 = new MeadowCard();
        FieldCard meadow6 = new MeadowCard();


        var exampleBoard = new ArrayList<ArrayList<Card>>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(meadow1,bee1,meadow4 ,meadow5)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(meadow2,meadow5,fish1,dragonfly2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(meadow3,bee2 ,dragonfly1 ,bee3)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(fox1  ,meadow6 ,bear2 ,rabbit2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(rabbit1,fish2 ,rabbit3  ,bear1)));

        setBoard(exampleBoard);
        assignNeighbours(board);

        assertEquals(9,bee1.count());
        assertEquals(9,bee2.count());
        assertEquals(0,bee3.count());
    }
}
