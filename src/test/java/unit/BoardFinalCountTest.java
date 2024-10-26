package unit;

import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.cards.*;
import com.eco.ecosystem.game.GameTable;
import com.eco.ecosystem.game.GamePlayer;
import com.eco.ecosystem.game.exceptions.InvalidCardTypeException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardFinalCountTest extends CardTest {

    @Test
    void testExampleBoardFinalCountFor1GamePlayer(){
        Card bear1 = new BearCard();
        Card wolf1 = new WolfCard();
        Card wolf2 = new WolfCard();
        Card wolf3 = new WolfCard();
        Card bee1 = new BeeCard();
        Card fox1 = new FoxCard();
        Card fox2 = new FoxCard();
        Card elk1 = new ElkCard();
        Card rabbit1 = new RabbitCard();
        Card fish1 = new FishCard();

        Card eagle1 = new EagleCard();
        Card dragonfly1 = new DragonflyCard();

        FieldCard river1 = new RiverCard();
        FieldCard river2 = new RiverCard();
        FieldCard river3 = new RiverCard();
        FieldCard river4 = new RiverCard();
        FieldCard meadow1 = new MeadowCard();
        FieldCard meadow2 = new MeadowCard();
        FieldCard meadow3 = new MeadowCard();
        FieldCard meadow4 = new MeadowCard();

        var expectedBoard =new ArrayList<ArrayList<Card>>();
        expectedBoard.add(new ArrayList<>(Arrays.asList(fox1   ,elk1   ,wolf1 ,river1 ,fox2)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(meadow1,meadow2,river2,river3 ,river4)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(meadow3,bee1   ,bear1 ,fish1  ,dragonfly1)));
        expectedBoard.add(new ArrayList<>(Arrays.asList(meadow4,wolf2  ,wolf3 ,rabbit1,eagle1)));

        setBoard(expectedBoard);
        assignNeighbours(board);
        GamePlayer player = new GamePlayer("",board);
        GameTable gameTable = new GameTable(List.of(player));

        List<GamePlayer> endGamePlayer = gameTable.endGame();
        assertEquals(12,player.getEcosystemGapPoints());
        assertEquals(0,player.getNumberOfGaps());
        assertEquals(3,player.getBoard().getWolfCount());
        assertEquals(12,player.getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(8,player.getGeneralPointCount().get(Card.CardType.RIVER));
        assertEquals(75,endGamePlayer.get(0).getSumOfPoints());

    }

    @Test
    void simulateExample4GamePlayerGame(){
        //player1
        Card player1bear1 = new BearCard();
        Card player1wolf1 = new WolfCard();
        Card player1wolf2 = new WolfCard();
        Card player1wolf3 = new WolfCard();
        Card player1bee1 = new BeeCard();
        Card player1fox1 = new FoxCard();
        Card player1fox2 = new FoxCard();
        Card player1elk1 = new ElkCard();
        Card player1rabbit1 = new RabbitCard();
        Card player1fish1 = new FishCard();
        Card player1eagle1 = new EagleCard();
        Card player1dragonfly1 = new DragonflyCard();
        FieldCard player1river1 = new RiverCard();
        FieldCard player1river2 = new RiverCard();
        FieldCard player1river3 = new RiverCard();
        FieldCard player1river4 = new RiverCard();
        FieldCard player1meadow1 = new MeadowCard();
        FieldCard player1meadow2 = new MeadowCard();
        FieldCard player1meadow3 = new MeadowCard();
        FieldCard player1meadow4 = new MeadowCard();

        var player1cardBoard =new ArrayList<ArrayList<Card>>();
        player1cardBoard.add(new ArrayList<>(Arrays.asList(player1fox1   ,player1elk1   ,player1wolf1 ,player1river1 ,player1fox2)));
        player1cardBoard.add(new ArrayList<>(Arrays.asList(player1meadow1,player1meadow2,player1river2,player1river3 ,player1river4)));
        player1cardBoard.add(new ArrayList<>(Arrays.asList(player1meadow3,player1bee1   ,player1bear1 ,player1fish1  ,player1dragonfly1)));
        player1cardBoard.add(new ArrayList<>(Arrays.asList(player1meadow4,player1wolf2  ,player1wolf3 ,player1rabbit1,player1eagle1)));
        //player2
        Card player2bear1 = new BearCard();
        Card player2bee1 = new BeeCard();
        Card player2bee2 = new BeeCard();
        Card player2fox1 = new FoxCard();
        Card player2fox2 = new FoxCard();
        Card player2elk1 = new ElkCard();
        Card player2elk2 = new ElkCard();
        Card player2elk3 = new ElkCard();
        Card player2rabbit1 = new RabbitCard();
        Card player2dragonfly1 = new DragonflyCard();
        FieldCard player2river1 = new RiverCard();
        FieldCard player2river2 = new RiverCard();
        FieldCard player2river3 = new RiverCard();
        FieldCard player2river4 = new RiverCard();
        FieldCard player2river5 = new RiverCard();
        FieldCard player2meadow1 = new MeadowCard();
        FieldCard player2meadow2 = new MeadowCard();
        FieldCard player2meadow3 = new MeadowCard();
        FieldCard player2meadow4 = new MeadowCard();
        FieldCard player2meadow5 = new MeadowCard();

        var player2cardBoard =new ArrayList<ArrayList<Card>>();
        player2cardBoard.add(new ArrayList<>(Arrays.asList(player2river1   ,player2fox1   ,player2elk1 ,player2bear1)));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(player2river2,player2meadow1,player2meadow2,player2bee1)));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(player2river3, player2meadow3,player2bee2 ,player2meadow4)));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(player2river4,player2dragonfly1  ,player2elk2 ,player2meadow5)));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(player2river5,player2elk3  ,player2rabbit1 ,player2fox2)));
        //player3
        Card player3bear1 = new BearCard();
        Card player3wolf1 = new WolfCard();
        Card player3wolf2 = new WolfCard();
        Card player3wolf3 = new WolfCard();
        Card player3bee1 = new BeeCard();
        Card player3bee2 = new BeeCard();
        Card player3fox1 = new FoxCard();
        Card player3elk1 = new ElkCard();
        Card player3elk2 = new ElkCard();
        Card player3rabbit1 = new RabbitCard();
        Card player3rabbit2 = new RabbitCard();
        Card player3rabbit3 = new RabbitCard();
        Card player3fish1 = new FishCard();
        Card player3eagle1 = new EagleCard();
        Card player3eagle2 = new EagleCard();
        Card player3eagle3 = new EagleCard();
        Card player3dragonfly1 = new DragonflyCard();
        FieldCard player3river1 = new RiverCard();
        FieldCard player3meadow1 = new MeadowCard();
        FieldCard player3meadow2 = new MeadowCard();

        var player3cardBoard =new ArrayList<ArrayList<Card>>();
        player3cardBoard.add(new ArrayList<>(Arrays.asList(player3meadow1,player3meadow2,player3bee1 ,player3elk1 ,player3wolf1)));
        player3cardBoard.add(new ArrayList<>(Arrays.asList(player3wolf2,player3bee2,player3bear1,player3eagle1 ,player3wolf3)));
        player3cardBoard.add(new ArrayList<>(Arrays.asList(player3dragonfly1,player3river1   ,player3eagle2 ,player3rabbit1  ,player3rabbit2)));
        player3cardBoard.add(new ArrayList<>(Arrays.asList(player3fox1,player3elk2  ,player3fish1 ,player3rabbit3,player3eagle3)));

        //player4
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


        var player1 = playerFabricMethod("player1",player1cardBoard);
        var player2 = playerFabricMethod("player2",player2cardBoard);
        var player3 = playerFabricMethod("player3",player3cardBoard);
        var player4 = playerFabricMethod("player4",player4cardBoard);

        GameTable gameTable = new GameTable(List.of(player1,player2,player3,player4));
        List<GamePlayer> calculatedGamePlayers = gameTable.endGame();

        assertEquals(12,calculatedGamePlayers.get(0).getEcosystemGapPoints());
        assertEquals(0,calculatedGamePlayers.get(0).getNumberOfGaps());
        assertEquals(3,calculatedGamePlayers.get(0).getBoard().getWolfCount());
        assertEquals(12,calculatedGamePlayers.get(0).getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(5,calculatedGamePlayers.get(0).getGeneralPointCount().get(Card.CardType.RIVER));
        assertEquals(72,calculatedGamePlayers.get(0).getSumOfPoints());

        assertEquals(7,calculatedGamePlayers.get(1).getEcosystemGapPoints());
        assertEquals(3,calculatedGamePlayers.get(1).getNumberOfGaps());
        assertEquals(0,calculatedGamePlayers.get(1).getBoard().getWolfCount());
        assertEquals(0,calculatedGamePlayers.get(1).getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(8,calculatedGamePlayers.get(1).getGeneralPointCount().get(Card.CardType.RIVER));
        assertEquals(63,calculatedGamePlayers.get(1).getSumOfPoints());

        assertEquals(12,calculatedGamePlayers.get(2).getEcosystemGapPoints());
        assertEquals(2,calculatedGamePlayers.get(2).getNumberOfGaps());
        assertEquals(3,calculatedGamePlayers.get(2).getBoard().getWolfCount());
        assertEquals(12,calculatedGamePlayers.get(2).getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(0,calculatedGamePlayers.get(2).getGeneralPointCount().get(Card.CardType.RIVER));
        assertEquals(74,calculatedGamePlayers.get(2).getSumOfPoints());

        assertEquals(12,calculatedGamePlayers.get(3).getEcosystemGapPoints());
        assertEquals(1,calculatedGamePlayers.get(3).getNumberOfGaps());
        assertEquals(3,calculatedGamePlayers.get(3).getBoard().getWolfCount());
        assertEquals(12,calculatedGamePlayers.get(3).getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(0,calculatedGamePlayers.get(3).getGeneralPointCount().get(Card.CardType.RIVER));
        assertEquals(65,calculatedGamePlayers.get(3).getSumOfPoints());

    }

    @Test
    void simulateExample4GamePlayerGame1() throws InvalidCardTypeException {
        //player1

        var rodekCardBoard =new ArrayList<ArrayList<Card>>();
        rodekCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.FOX),Card.from(Card.CardType.RABBIT) ,Card.from(Card.CardType.RABBIT) ,Card.from(Card.CardType.DRAGONFLY) ,Card.from(Card.CardType.RIVER))));
        rodekCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.FOX),Card.from(Card.CardType.EAGLE),Card.from(Card.CardType.EAGLE),Card.from(Card.CardType.FISH) ,Card.from(Card.CardType.RIVER))));
        rodekCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.BEE)   ,Card.from(Card.CardType.RABBIT) ,Card.from(Card.CardType.BEAR) ,Card.from(Card.CardType.RIVER))));
        rodekCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.MEADOW)  ,Card.from(Card.CardType.WOLF),Card.from(Card.CardType.WOLF),Card.from(Card.CardType.WOLF))));
        //player2


        var michlCardBoard =new ArrayList<ArrayList<Card>>();
        michlCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.BEAR),Card.from(Card.CardType.WOLF),Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.RABBIT),Card.from(Card.CardType.EAGLE))));
        michlCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.FOX),Card.from(Card.CardType.ELK),Card.from(Card.CardType.RIVER),Card.from(Card.CardType.FISH),Card.from(Card.CardType.BEAR))));
        michlCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.ELK), Card.from(Card.CardType.WOLF),Card.from(Card.CardType.RIVER) ,Card.from(Card.CardType.DRAGONFLY),Card.from(Card.CardType.BEAR))));
        michlCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.EAGLE),Card.from(Card.CardType.FISH) ,Card.from(Card.CardType.RIVER) ,Card.from(Card.CardType.RIVER),Card.from(Card.CardType.FISH))));
        //player3

        var tataCardBoard =new ArrayList<ArrayList<Card>>();
        tataCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.ELK),Card.from(Card.CardType.BEAR),Card.from(Card.CardType.FISH),Card.from(Card.CardType.BEE),Card.from(Card.CardType.MEADOW))));
        tataCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.BEAR),Card.from(Card.CardType.FISH),Card.from(Card.CardType.RIVER),Card.from(Card.CardType.DRAGONFLY),Card.from(Card.CardType.ELK))));
        tataCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.FISH),Card.from(Card.CardType.EAGLE),Card.from(Card.CardType.FOX),Card.from(Card.CardType.ELK),Card.from(Card.CardType.MEADOW))));
        tataCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.EAGLE),Card.from(Card.CardType.RABBIT),Card.from(Card.CardType.ELK),Card.from(Card.CardType.FOX),Card.from(Card.CardType.WOLF))));

        //player4


        var emiCardBoard =new ArrayList<ArrayList<Card>>();
        emiCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.WOLF),Card.from(Card.CardType.BEAR),Card.from(Card.CardType.FISH),Card.from(Card.CardType.FOX),Card.from(Card.CardType.FOX))));
        emiCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.WOLF),Card.from(Card.CardType.BEE),Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.EAGLE),Card.from(Card.CardType.RABBIT))));
        emiCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.WOLF),Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.ELK))));
        emiCardBoard.add(new ArrayList<>(Arrays.asList(Card.from(Card.CardType.WOLF),Card.from(Card.CardType.ELK),Card.from(Card.CardType.MEADOW),Card.from(Card.CardType.BEE),Card.from(Card.CardType.FOX))));


        var player1 = playerFabricMethod("Rodek",rodekCardBoard);
        var player4 = playerFabricMethod("Emi",emiCardBoard);
        var player2 = playerFabricMethod("Michal",michlCardBoard);
        var player3 = playerFabricMethod("Tata",tataCardBoard);


        GameTable gameTable = new GameTable(List.of(player1,player2,player3,player4));
        List<GamePlayer> calculatedGamePlayers = gameTable.endGame();
        assertEquals(player1.getSumOfPoints(),71);
        assertEquals(player2.getSumOfPoints(),50);
        assertEquals(player3.getSumOfPoints(),53);
        assertEquals(player4.getSumOfPoints(),72);

    }
    private GamePlayer playerFabricMethod(String name, ArrayList<ArrayList<Card>> cardBoard){
        var board = new Board();
        var player = new GamePlayer(name, board);
        setBoardAndAssignNeighbours(player,cardBoard);
        return player;
    }
}
