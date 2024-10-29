package unit;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.CardStack;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class GameDtoTest {

    @Test
    void startGameTest(){

        var game = new GameDto(UUID.randomUUID(),
                List.of(
                        new Player(UUID.randomUUID(),"player1",List.of(),List.of(List.of()),0),
                        new Player(UUID.randomUUID(),"player2",List.of(),List.of(List.of()),0),
                        new Player(UUID.randomUUID(),"player3",List.of(),List.of(List.of()),0),
                        new Player(UUID.randomUUID(),"player4",List.of(),List.of(List.of()),0)
                ),
                CardStack.initCardStack().stream().map(PlayerCard::new).toList(),
                0);
        game.startGame();
        assertEquals(11,game.getPlayers().get(0).getCardsInHand().size());
        assertEquals(11,game.getPlayers().get(1).getCardsInHand().size());
        assertEquals(11,game.getPlayers().get(2).getCardsInHand().size());
        assertEquals(11,game.getPlayers().get(3).getCardsInHand().size());
        assertEquals(130-44,game.getCardStack().size());

    }
}
