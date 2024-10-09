package unit.cards;

import game.cards.*;
import org.junit.jupiter.api.Test;
import unit.CardTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EagleCardTest extends CardTest {

    @Test
    void testEagleCardCount(){
        Card bear4 = new BearCard();

        Card fox1 = new FoxCard();
        Card fish1 = new FishCard();
        Card fish2 = new FishCard();
        Card fish3 = new FishCard();
        Card fish4 = new FishCard();
        Card rabbit1 = new RabbitCard();
        Card rabbit2 = new RabbitCard();
        Card rabbit3 = new RabbitCard();
        Card rabbit4 = new RabbitCard();
        Card eagle1 = new EagleCard();
        Card eagle2 = new EagleCard();

        FieldCard river2 = new RiverCard();

        FieldCard river4 = new RiverCard();
        FieldCard river5 = new RiverCard();
        FieldCard river6 = new RiverCard();
        FieldCard meadow1 = new MeadowCard();
        FieldCard meadow2 = new MeadowCard();
        FieldCard meadow3 = new MeadowCard();
        FieldCard meadow5 = new MeadowCard();
        FieldCard meadow6 = new MeadowCard();

        var exampleBoard = new ArrayList<ArrayList<Card>>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(eagle1,river2,fish2 ,meadow3)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(meadow6  ,rabbit1,river4,river5)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(fish1  ,rabbit2 ,river6 ,meadow2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(bear4  ,meadow5 ,fish4 ,meadow1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(rabbit3  ,eagle2 ,rabbit4  ,fish3)));

        setBoard(exampleBoard);
        assignNeighbours(board);

        assertEquals(6,eagle1.count());
        assertEquals(10,eagle2.count());
    }

    @Test
    void testEagleSshape(){
        Card player4bear1 = new BearCard();
        Card player4wolf1 = new WolfCard();
        Card player4wolf2 = new WolfCard();
        Card player4wolf3 = new WolfCard();
        Card player4bee1 = new BeeCard();
        Card player4fox1 = new FoxCard();
        Card player4fox2 = new FoxCard();
        Card player4elk1 = new ElkCard();
        Card player4rabbit1 = new RabbitCard();
        Card player4rabbit2 = new RabbitCard();
        Card player4fish1 = new FishCard();
        Card player4fish2 = new FishCard();
        Card player4eagle1 = new EagleCard();
        Card player4dragonfly1 = new DragonflyCard();
        FieldCard player4river1 = new RiverCard();
        FieldCard player4river2 = new RiverCard();
        FieldCard player4river3 = new RiverCard();
        FieldCard player4meadow1 = new MeadowCard();
        FieldCard player4meadow2 = new MeadowCard();
        FieldCard player4meadow3 = new MeadowCard();

        var player4cardBoard =new ArrayList<ArrayList<Card>>();
        player4cardBoard.add(new ArrayList<>(Arrays.asList(player4meadow1,player4meadow2 ,player4rabbit1    ,player4rabbit2 ,player4wolf1)));
        player4cardBoard.add(new ArrayList<>(Arrays.asList(player4bee1   ,player4meadow3 ,player4eagle1     ,player4fox1    ,player4elk1)));
        player4cardBoard.add(new ArrayList<>(Arrays.asList(player4bear1  ,player4fish1   ,player4fish2      ,player4river1  ,player4wolf2)));
        player4cardBoard.add(new ArrayList<>(Arrays.asList(player4river2 ,player4river3  ,player4dragonfly1 ,player4fox2    ,player4wolf3)));

        setBoard(player4cardBoard);
        assignNeighbours(board);

        assertEquals(8,player4eagle1.count());
    }
}
