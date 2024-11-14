package service;

import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.Card;
import com.eco.ecosystem.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest extends ServiceTest{

    private Player player;
    private List<PlayerCard> hand;
    @Mock
    ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    PlayerService playerService;

    @BeforeEach
    void setUp() {
        assertNotNull(reactiveMongoTemplate, "ReactiveMongoTemplate should be mocked and initialized");
        var board = new ArrayList<List<PlayerCard>>();
        hand = List.of(new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.RIVER)),
                new PlayerCard(Card.from(Card.CardType.BEAR)),
                new PlayerCard(Card.from(Card.CardType.MEADOW)),
                new PlayerCard(Card.from(Card.CardType.RABBIT)),
                new PlayerCard(Card.from(Card.CardType.MEADOW)),
                new PlayerCard(Card.from(Card.CardType.FOX))
        );
        board.add(new ArrayList<>(Arrays.asList(null, null,                                         null,                                          null)));
        board.add(new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.EAGLE)),null)));
        board.add(new ArrayList<>(Arrays.asList(null, null,                                         new PlayerCard(Card.from(Card.CardType.FISH)), null)));
        board.add(new ArrayList<>(Arrays.asList(null, null,                                         null,                                          null)));
        player = new Player(playerID, "name",
                hand,
                board,
        0);

    }

    @Test
    void shouldProperlyReturnAvailableMoves() {
        var game = new Game(gameID, List.of(player), List.of(), 4);
        when(reactiveMongoTemplate.findOne(any(), any())).thenReturn(Mono.just(game));
        var availableMoves = playerService.getAvailableMoves(gameID, playerID).block();
        var expectedListOfAvailableSlots = List.of(
                new Slot(0, 1),
                new Slot(0, 2),
                new Slot(1, 0),
                new Slot(1, 3),
                new Slot(2, 1),
                new Slot(2, 3),
                new Slot(3, 2));

        assert availableMoves != null;
        assertThat(availableMoves.getAvailableSlots()).containsExactlyInAnyOrderElementsOf(expectedListOfAvailableSlots);
        assertThat(availableMoves.getCardsInHand()).containsExactlyInAnyOrderElementsOf(hand);
    }
}
