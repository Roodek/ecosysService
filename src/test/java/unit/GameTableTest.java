package unit;

import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.GamePlayer;
import com.eco.ecosystem.game.GameTable;
import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.cards.*;
import org.junit.jupiter.api.Test;
import utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTableTest extends CardTest {

    @Test
    void testSwapHands() {
        var player1 = initPlayerHand("player1",List.of(new PlayerCard(Card.from(Card.CardType.BEE))));
        var player2 = initPlayerHand("player2",List.of(new PlayerCard(Card.from(Card.CardType.ELK))));
        var player3 = initPlayerHand("player3",List.of(new PlayerCard(Card.from(Card.CardType.RIVER))));
        var gameTable = new Game();
        gameTable.setPlayers(List.of(player1,player2,player3));
        var game = AppUtils.gameEntityToDto(gameTable);

        assertEquals(Card.CardType.BEE.toString(),game.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.ELK.toString(),game.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.RIVER.toString(), game.getPlayers().get(2).getCardsInHand().get(0).getCardType());

        game.swapPlayersHands();

        assertEquals(Card.CardType.ELK.toString(),game.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals( Card.CardType.RIVER.toString(), game.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.BEE.toString(), game.getPlayers().get(2).getCardsInHand().get(0).getCardType());

        game.setTurn(10);
        game.swapPlayersHands();

        assertEquals(Card.CardType.RIVER.toString(),game.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.BEE.toString(),game.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.ELK.toString(), game.getPlayers().get(2).getCardsInHand().get(0).getCardType());

        game.setTurn(11);
        game.swapPlayersHands();

        assertEquals(Card.CardType.ELK.toString(),game.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals( Card.CardType.RIVER.toString(), game.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.BEE.toString(), game.getPlayers().get(2).getCardsInHand().get(0).getCardType());

        game.setTurn(12);
        game.swapPlayersHands();

        assertEquals(Card.CardType.BEE.toString(),game.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.ELK.toString(),game.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.RIVER.toString(), game.getPlayers().get(2).getCardsInHand().get(0).getCardType());
    }

    @Test
    void testCompareRiversAndWolves(){
        //player1
        var player1cardBoard =new ArrayList<ArrayList<Card>>();
        player1cardBoard.add(new ArrayList<>(Arrays.asList(new FoxCard()   ,new ElkCard()   ,new WolfCard() ,new RiverCard() ,new FoxCard())));
        player1cardBoard.add(new ArrayList<>(Arrays.asList(new MeadowCard(),new MeadowCard(),new RiverCard(),new RiverCard() ,new RiverCard())));
        player1cardBoard.add(new ArrayList<>(Arrays.asList(new MeadowCard(),new BeeCard()   ,new BearCard() ,new FishCard()  ,new DragonflyCard())));
        player1cardBoard.add(new ArrayList<>(Arrays.asList(new MeadowCard(),new WolfCard()  ,new WolfCard() ,new RabbitCard(),new EagleCard())));
        //player2
        var player2cardBoard =new ArrayList<ArrayList<Card>>();
        player2cardBoard.add(new ArrayList<>(Arrays.asList(new RiverCard()   ,new FoxCard()   ,new ElkCard() ,new BearCard())));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(new RiverCard(),new MeadowCard(),new MeadowCard(),new BeeCard())));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(new FishCard(), new MeadowCard(),new MeadowCard() ,new MeadowCard())));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(new BeeCard(),new DragonflyCard()  ,new ElkCard() ,new MeadowCard())));
        player2cardBoard.add(new ArrayList<>(Arrays.asList(new BearCard(),new ElkCard()  ,new WolfCard() ,new FoxCard())));
        //player3
        var player3cardBoard =new ArrayList<ArrayList<Card>>();
        player3cardBoard.add(new ArrayList<>(Arrays.asList(new MeadowCard(),new MeadowCard(),new BeeCard() ,new ElkCard() ,new WolfCard())));
        player3cardBoard.add(new ArrayList<>(Arrays.asList(new WolfCard(),new BeeCard(),new BearCard(),new EagleCard() ,new WolfCard())));
        player3cardBoard.add(new ArrayList<>(Arrays.asList(new RiverCard(),new RiverCard()   ,new EagleCard() ,new RabbitCard()  ,new RabbitCard())));
        player3cardBoard.add(new ArrayList<>(Arrays.asList(new FoxCard(),new ElkCard()  ,new FishCard() ,new RabbitCard(),new EagleCard())));


        var player1 = playerFabricMethod("player1",player1cardBoard);
        var player2 = playerFabricMethod("player2",player2cardBoard);
        var player3 = playerFabricMethod("player3",player3cardBoard);

        GameTable gameTable = new GameTable(List.of(player1,player2,player3));
        List<GamePlayer> calculatedGamePlayers = gameTable.endGame();
        assertEquals(12,calculatedGamePlayers.get(0).getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(12,calculatedGamePlayers.get(2).getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(4,calculatedGamePlayers.get(1).getGeneralPointCount().get(Card.CardType.WOLF));
        assertEquals(8,calculatedGamePlayers.get(0).getGeneralPointCount().get(Card.CardType.RIVER));
        assertEquals(5,calculatedGamePlayers.get(1).getGeneralPointCount().get(Card.CardType.RIVER));
        assertEquals(5,calculatedGamePlayers.get(2).getGeneralPointCount().get(Card.CardType.RIVER));
    }
    private Player initPlayerHand(String name, List<PlayerCard> hand){
        var board = new Board();
        var player = new Player();
        player.setName(name);
        player.setBoard(board.toResponseBoard());
        player.setCardsInHand(hand);
        return player;
    }
    private GamePlayer playerFabricMethod(String name, ArrayList<ArrayList<Card>> cardBoard) {
        var board = new Board();
        var player = new GamePlayer(name, board);
        setBoardAndAssignNeighbours(player, cardBoard);
        return player;
    }
}
