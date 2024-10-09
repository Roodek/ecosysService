package unit;

import game.CardStack;
import game.cards.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardStackTest {

    //meadows x20
    //rivers x20
    //elks x12
    //bears x12
    //fox x12
    //wolf x12
    //fish x10
    //dragonflies x8
    //bees x8
    //eagles x8
    //rabbits x8
    @Test
    void testInitBoard() {
        var cards = CardStack.initCardStack();
        assertEquals(20, cards.stream().filter(card -> card.getType() == Card.CardType.MEADOW).toList().size());
        assertEquals(20, cards.stream().filter(card -> card.getType() == Card.CardType.RIVER).toList().size());
        assertEquals(12, cards.stream().filter(card -> card.getType() == Card.CardType.ELK).toList().size());
        assertEquals(12, cards.stream().filter(card -> card.getType() == Card.CardType.WOLF).toList().size());
        assertEquals(12, cards.stream().filter(card -> card.getType() == Card.CardType.BEAR).toList().size());
        assertEquals(12, cards.stream().filter(card -> card.getType() == Card.CardType.FOX).toList().size());
        assertEquals(10, cards.stream().filter(card -> card.getType() == Card.CardType.FISH).toList().size());
        assertEquals(8, cards.stream().filter(card -> card.getType() == Card.CardType.DRAGONFLY).toList().size());
        assertEquals(8, cards.stream().filter(card -> card.getType() == Card.CardType.BEE).toList().size());
        assertEquals(8, cards.stream().filter(card -> card.getType() == Card.CardType.EAGLE).toList().size());
        assertEquals(8, cards.stream().filter(card -> card.getType() == Card.CardType.RABBIT).toList().size());
        System.out.println(cards);
    }

    @Test
    void testDealCard() {
        var cards = CardStack.initCardStack();
        var card = CardStack.dealCard(cards);
        assertNotNull(card);
        assertEquals(129, cards.size());
    }

    @Test
    void testDealFirstAndSecondHand() {
        var cards = CardStack.initCardStack();
        var player1FirstHand = CardStack.dealFirstHandFromStack(cards);
        var player2FirstHand = CardStack.dealFirstHandFromStack(cards);
        var player3FirstHand = CardStack.dealFirstHandFromStack(cards);
        var player4FirstHand = CardStack.dealFirstHandFromStack(cards);
        assertEquals(130 - 44, cards.size());
        assertEquals(11, player1FirstHand.size());
        assertEquals(11, player2FirstHand.size());
        assertEquals(11, player3FirstHand.size());
        assertEquals(11, player4FirstHand.size());

        player1FirstHand = CardStack.dealSecondHandFromStack(cards);
        player2FirstHand = CardStack.dealSecondHandFromStack(cards);
        player3FirstHand = CardStack.dealSecondHandFromStack(cards);
        player4FirstHand = CardStack.dealSecondHandFromStack(cards);

        assertEquals(130 - 44 - 40, cards.size());
        assertEquals(10, player1FirstHand.size());
        assertEquals(10, player2FirstHand.size());
        assertEquals(10, player3FirstHand.size());
        assertEquals(10, player4FirstHand.size());


    }
}
