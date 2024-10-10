package unit;


import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.*;
import com.eco.ecosystem.game.exceptions.InvalidMoveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardTest {

    private Board board;
    @BeforeEach
    void prep(){
        board = new Board();
    }

    @AfterEach
    void finalizeWith(){
        board.printBoard();
        System.out.println();
    }

    @Test
    void constructorFromRawArrayOfString(){
        var rawArray =new ArrayList<List<PlayerCard>>();
        rawArray.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
        rawArray.add(new ArrayList<>(Arrays.asList(new PlayerCard("fox"),new PlayerCard("elk"),new PlayerCard("bee"),new PlayerCard("river"),new PlayerCard("wolf"))));
        rawArray.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));

        var board = new Board(rawArray);
        assertEquals(1,board.getSizeVertical());
        assertEquals(5,board.getSizeHorizontal());
        assertEquals(5,board.getMaxHorizontalSize());
        assertEquals(4,board.getMaxVerticalSize());

        var rawArray1 =new ArrayList<List<String>>();
        rawArray1.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
        rawArray1.add(new ArrayList<>(Arrays.asList("fox","elk","bee","river","wolf")));
        rawArray1.add(new ArrayList<>(Arrays.asList(null,null,"river",null,null)));
        rawArray1.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));

//        var board1 = new Board(rawArray1);
//        assertEquals(2,board1.getSizeVertical());
//        assertEquals(5,board1.getSizeHorizontal());
//        assertEquals(5,board1.getMaxHorizontalSize());
//        assertEquals(4,board1.getMaxVerticalSize());
//
//        var rawArray2 =new ArrayList<List<String>>();
//        rawArray2.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
//        rawArray2.add(new ArrayList<>(Arrays.asList(null,null,"meadow",null,null)));
//        rawArray2.add(new ArrayList<>(Arrays.asList(null,"elk","bee","river",null)));
//        rawArray2.add(new ArrayList<>(Arrays.asList(null,null,"river",null,null)));
//        rawArray2.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
//
//        var board2 = new Board(rawArray2);
//        assertEquals(3,board2.getSizeVertical());
//        assertEquals(3,board2.getSizeHorizontal());
//        assertEquals(5,board2.getMaxHorizontalSize());
//        assertEquals(5,board2.getMaxVerticalSize());



    }
    @Test
    void putCardAtCoordinates() throws InvalidMoveException {
        var wolf = new WolfCard();
        var river = new RiverCard();
        var bee = new BeeCard();
        var elk = new ElkCard();
        var fox = new FoxCard();
        var expectedBoard = new ArrayList<ArrayList<Card>>();
        for(int i =0;i<3;i++){
            expectedBoard.add(new ArrayList<>());
            for(int j =0;j<3;j++){
                expectedBoard.get(i).add(null);
            }
        }
        expectedBoard.get(1).set(1,wolf);
        board.putFirstCard(wolf);
        assertEquals(1,board.getSizeHorizontal());
        assertEquals(1,board.getSizeVertical());

        assertEquals(expectedBoard,board.getCardBoard());
        board.putCard(river,0,1);
        assertEquals(1,board.getSizeHorizontal());
        assertEquals(2,board.getSizeVertical());

        expectedBoard.set(0, new ArrayList<>(Arrays.asList(null,river,null)));
        expectedBoard.add(0, new ArrayList<>(Arrays.asList(null,null,null)));
        assertEquals(expectedBoard,board.getCardBoard());

        board.putCard(bee,1,2);
        assertEquals(2,board.getSizeHorizontal());
        assertEquals(2,board.getSizeVertical());
        expectedBoard.forEach(row->row.add(null));
        expectedBoard.get(1).set(2,bee);

        assertEquals(expectedBoard,board.getCardBoard());

        board.putCard(elk,2,0);
        assertEquals(3,board.getSizeHorizontal());
        assertEquals(2,board.getSizeVertical());
        expectedBoard.get(2).set(0,elk);
        expectedBoard.forEach(row->row.add(0,null));

        assertEquals(expectedBoard,board.getCardBoard());

        board.putCard(fox,3,2);
        assertEquals(3,board.getSizeHorizontal());
        assertEquals(3,board.getSizeVertical());
        expectedBoard.get(3).set(2,fox);
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));

        assertEquals(expectedBoard,board.getCardBoard());
    }

    @Test
    void checkIfBoardLimitedProperlyTo4x5() throws InvalidMoveException {
        var wolf = new WolfCard();
        var river = new RiverCard();
        var bee = new BeeCard();
        var elk = new ElkCard();
        var fox = new FoxCard();

        var meadow = new MeadowCard();
        var dragonfly = new DragonflyCard();
        var fish= new FishCard();


        board.putFirstCard(wolf);
        board.putCard(river,1,0);
        board.putCard(bee,1,0);
        board.putCard(elk,1,0);
        board.putCard(fox,1,0);

        var expectedBoard =new ArrayList<ArrayList<Card>>();
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox,elk,bee,river,wolf)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));

        assertEquals(1, board.getSizeVertical());
        assertEquals(5, board.getSizeHorizontal());

        assertEquals(expectedBoard,board.getCardBoard());
        board.putCard(meadow,0,4);
        board.putCard(dragonfly,0,4);
        board.putCard(fish,0,4);

        var expectedBoard2 = new ArrayList<ArrayList<Card>>();
        expectedBoard2.add(new ArrayList<>(Arrays.asList(null,null,null,null,fish)));
        expectedBoard2.add(new ArrayList<>(Arrays.asList(null,null,null,null,dragonfly)));
        expectedBoard2.add(new ArrayList<>(Arrays.asList(null,null,null,null,meadow)));
        expectedBoard2.add(new ArrayList<>(Arrays.asList(fox,elk,bee,river,wolf)));


        assertEquals(expectedBoard2,board.getCardBoard());
    }

    @Test
    void checkIfBoardLimitedProperlyTo5x4() throws InvalidMoveException {
        var wolf = new WolfCard();
        var river = new RiverCard();
        var bee = new BeeCard();
        var elk = new ElkCard();
        var fox = new FoxCard();

        var meadow = new MeadowCard();
        var dragonfly = new DragonflyCard();
        var fish= new FishCard();

        board.putFirstCard(wolf);
        board.putCard(river,0,1);
        board.putCard(bee,0,1);
        board.putCard(elk,0,1);
        board.putCard(fox,0,1);

        var expectedBoard =new ArrayList<ArrayList<Card>>();
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,fox,null)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,elk,null)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,bee,null)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,river,null)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(null,wolf,null)));

        assertEquals(5, board.getSizeVertical());
        assertEquals(1, board.getSizeHorizontal());


        assertEquals(expectedBoard,board.getCardBoard());
        board.putCard(meadow,0,0);
        board.putCard(dragonfly,0,0);
        board.putCard(fish,0,0);

        var expectedBoard2 = new ArrayList<ArrayList<Card>>();
        expectedBoard2.add(new ArrayList<>(Arrays.asList(fish,dragonfly,meadow,fox)));
        expectedBoard2.add(new ArrayList<>(Arrays.asList(null,null,null,elk)));
        expectedBoard2.add(new ArrayList<>(Arrays.asList(null,null,null,bee)));
        expectedBoard2.add(new ArrayList<>(Arrays.asList(null,null,null,river)));
        expectedBoard2.add(new ArrayList<>(Arrays.asList(null,null,null,wolf)));

        assertEquals(expectedBoard2,board.getCardBoard());
        Card rabbit = new RabbitCard();
        board.putCard(rabbit,2,2);
        board.rabbitSwap(new Slot(2,2),new Slot(2,3));

        var expectedBoard3 = new ArrayList<ArrayList<Card>>();
        expectedBoard3.add(new ArrayList<>(Arrays.asList(fish,dragonfly,meadow,fox)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,null,elk)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,bee,rabbit)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,null,river)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,null,wolf)));

        assertEquals(expectedBoard3,board.getCardBoard());

    }
    @Test
    void testSwapCards() throws InvalidMoveException {
        var wolf = new WolfCard();
        var river = new RiverCard();
        var bee = new BeeCard();
        var elk = new ElkCard();
        var fox = new FoxCard();

        var meadow = new MeadowCard();
        var dragonfly = new DragonflyCard();
        var fish= new FishCard();
        var rabbit = new RabbitCard();

        board.putFirstCard(wolf);
        board.putCard(river,0,1);
        board.putCard(bee,0,1);
        board.putCard(elk,0,1);
        board.putCard(fox,0,1);
        board.putCard(meadow,0,0);
        board.putCard(dragonfly,0,0);
        board.putCard(fish,0,0);

        board.putCard(rabbit,2,2);
        board.printBoard();

        board.rabbitSwap(new Slot(2,2),new Slot(2,3));

        var expectedBoard3 = new ArrayList<ArrayList<Card>>();
        expectedBoard3.add(new ArrayList<>(Arrays.asList(fish,dragonfly,meadow,fox)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,null,elk)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,bee,rabbit)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,null,river)));
        expectedBoard3.add(new ArrayList<>(Arrays.asList(null,null,null,wolf)));

        assertEquals(expectedBoard3,board.getCardBoard());
    }

    @Test
    void putAllCardsAndSeeIfNeighboursAssignedProperly() throws InvalidMoveException {
        var wolf1 = new WolfCard();
        var wolf2 = new WolfCard();
        var wolf3 = new WolfCard();
        var river1 = new RiverCard();
        var river2 = new RiverCard();
        var river3 = new RiverCard();
        var river4 = new RiverCard();
        var bee1 = new BeeCard();
        var elk1 = new ElkCard();
        var fox1 = new FoxCard();
        var fox2 = new FoxCard();
        var fish1 = new FishCard();
        var meadow1 = new MeadowCard();
        var meadow2 = new MeadowCard();
        var meadow3 = new MeadowCard();
        var meadow4 = new MeadowCard();
        var bear1 = new BearCard();
        var dragonfly1 = new DragonflyCard();
        var rabbit1 = new RabbitCard();
        var eagle1 = new EagleCard();

        board.putFirstCard(wolf1);
        board.putCard(elk1,1,0);
        board.putCard(fox1,1,0);
        board.putCard(river1,1,4);
        board.putCard(fox2,1,5);//board gets cut down to 5x3
        assertThrows(InvalidMoveException.class, ()->board.putCard(fox2,1,0));
        assertThrows(IndexOutOfBoundsException.class, ()->board.putCard(fox2,1,5));
        board.putCard(meadow1,2,0);
        board.putCard(meadow2,2,1);
        board.putCard(river2,2,2);
        board.putCard(river3,2,3);
        board.putCard(river4,2,4);
        board.putCard(meadow3,3,0);
        board.putCard(bee1,3,1);
        board.putCard(bear1,3,2);
        board.putCard(fish1,3,3);
        board.putCard(dragonfly1,3,4);
        board.putCard(meadow4,4,0);//board gets cut down to 5x4
        board.putCard(wolf2,3,1);
        board.putCard(wolf3,3,2);
        board.putCard(rabbit1,3,3);
        board.putCard(eagle1,3,4);

        var expectedBoard =new ArrayList<ArrayList<Card>>();
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1   ,elk1   ,wolf1 ,river1 ,fox2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(meadow1,meadow2,river2,river3 ,river4)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(meadow3,bee1   ,bear1 ,fish1  ,dragonfly1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(meadow4,wolf2  ,wolf3 ,rabbit1,eagle1)));

        assertEquals(expectedBoard,board.getCardBoard());

        verifyCardNeighbours(fox1,null,meadow1,null,elk1);
        verifyCardNeighbours(elk1, null, meadow2, fox1, wolf1);
        verifyCardNeighbours(wolf1, null,river2, elk1, river1);
        verifyCardNeighbours(river1, null, river3,wolf1,fox2);
        verifyCardNeighbours(fox2, null, river4,river1,null);

        verifyCardNeighbours(meadow1, fox1,meadow3,null,meadow2);
        verifyCardNeighbours(meadow2,elk1,bee1,meadow1,river2);
        verifyCardNeighbours(river2, wolf1,bear1,meadow2,river3);
        verifyCardNeighbours(river3, river1,fish1,river2,river4);
        verifyCardNeighbours(river4, fox2,dragonfly1,river3,null);

        verifyCardNeighbours(meadow3, meadow1, meadow4,null,bee1);
        verifyCardNeighbours(bee1, meadow2,wolf2,meadow3,bear1);
        verifyCardNeighbours(bear1, river2,wolf3,bee1,fish1);
        verifyCardNeighbours(fish1, river3,rabbit1,bear1,dragonfly1);
        verifyCardNeighbours(dragonfly1, river4,eagle1,fish1,null);

        verifyCardNeighbours(meadow4,meadow3,null,null, wolf2);
        verifyCardNeighbours(wolf2, bee1, null,meadow4,wolf3);
        verifyCardNeighbours(wolf3, bear1,null,wolf2,rabbit1);
        verifyCardNeighbours(rabbit1, fish1,null,wolf3,eagle1);
        verifyCardNeighbours(eagle1,dragonfly1,null,rabbit1,null);

    }


    private static void verifyCardNeighbours(Card targetCard, Card upper, Card bottom,Card left, Card right) {
        assertEquals(Stream.of(upper,bottom,left,right).filter(Objects::nonNull).toList().size(), targetCard.getNeighbours().size());
        assertEquals(upper, targetCard.getUpperNeighbour());
        assertEquals(bottom, targetCard.getBottomNeighbour());
        assertEquals(left, targetCard.getLeftNeighbour());
        assertEquals(right, targetCard.getRightNeighbour());

    }
}