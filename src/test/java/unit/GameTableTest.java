package unit;

import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.cards.Card;
import org.junit.jupiter.api.Test;
import utils.AppUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTableTest {

    @Test
    void testSwapHands() {
        var player1 = initPlayerHand("player2",List.of(new PlayerCard(Card.from(Card.CardType.BEE))));
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

        assertEquals(Card.CardType.RIVER.toString(),game.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals( Card.CardType.BEE.toString(), game.getPlayers().get(1).getCardsInHand().get(0).getCardType());
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

    private Player initPlayerHand(String name, List<PlayerCard> hand){
        var board = new Board();
        var player = new Player();
        player.setName(name);
        player.setBoard(board.toResponseBoard());
        player.setCardsInHand(hand);
        return player;
    }
}
