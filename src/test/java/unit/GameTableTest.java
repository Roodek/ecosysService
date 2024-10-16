package unit;

import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.cards.Card;
import org.junit.jupiter.api.Test;

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

        assertEquals(Card.CardType.BEE.toString(),gameTable.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.ELK.toString(),gameTable.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.RIVER.toString(), gameTable.getPlayers().get(2).getCardsInHand().get(0).getCardType());

        gameTable.swapPlayersHands(Game.SwapDirection.LEFT);

        assertEquals(Card.CardType.ELK.toString(),gameTable.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals( Card.CardType.RIVER.toString(), gameTable.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.BEE.toString(), gameTable.getPlayers().get(2).getCardsInHand().get(0).getCardType());

        gameTable.swapPlayersHands(Game.SwapDirection.RIGHT);

        assertEquals(Card.CardType.BEE.toString(),gameTable.getPlayers().get(0).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.ELK.toString(),gameTable.getPlayers().get(1).getCardsInHand().get(0).getCardType());
        assertEquals(Card.CardType.RIVER.toString(), gameTable.getPlayers().get(2).getCardsInHand().get(0).getCardType());
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
