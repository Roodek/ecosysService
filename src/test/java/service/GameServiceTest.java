package service;

import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.entities.SelectedMove;
import com.eco.ecosystem.game.CardStack;
import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.Card;
import com.eco.ecosystem.services.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest extends ServiceTest {


    private UUID playerID2 = UUID.randomUUID();
    private UUID playerID3 = UUID.randomUUID();
    @Mock
    ReactiveMongoTemplate reactiveMongoTemplate;
    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    GameService gameService;

    @Test
    void shouldGetGameDataWithOnlySpecificPLayersHand() {
        var hand1 = List.of(new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK))
        );
        var hand2 = List.of(new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX))
        );
        var hand3 = List.of(new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE))
        );
        List<List<PlayerCard>> board1 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), null)),
                new ArrayList<>(Arrays.asList(null, null, new PlayerCard(Card.from(Card.CardType.ELK)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));
        List<List<PlayerCard>> board2 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)), null)),
                new ArrayList<>(Arrays.asList(null, null, new PlayerCard(Card.from(Card.CardType.FOX)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));
        List<List<PlayerCard>> board3 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)), null)),
                new ArrayList<>(Arrays.asList(null, null, new PlayerCard(Card.from(Card.CardType.BEE)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));

        var player1 = new Player(playerID, "player1",
                hand1,
                null,
                board1,
                0);
        var player2 = new Player(playerID2, "player2",
                hand2,
                null,
                board2,
                0);
        var player3 = new Player(playerID3, "player3",
                hand3,
                null,
                board3,
                0);
        var game = new Game(gameID, List.of(player1,player2,player3), List.of(), 4);


        when(reactiveMongoTemplate.findById(eq(gameID), eq(Game.class))).thenReturn(Mono.just(game));

        var result = gameService.getGameForSpecificPlayer(gameID, playerID).block();

        assert result != null;
        assertEquals(3,result.getPlayers().size());
        assertEquals(4,result.getPlayers().stream().filter(player -> player.getId()==playerID).toList().get(0).getCardsInHand().size());
        assertEquals(0,result.getPlayers().stream().filter(player -> player.getId()==playerID2).toList().get(0).getCardsInHand().size());
        assertEquals(0,result.getPlayers().stream().filter(player -> player.getId()==playerID3).toList().get(0).getCardsInHand().size());
    }
    @Test
    void shouldNotUpdateGameWhenTurnNotEnded(){
        var hand1 = List.of(
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK))
        );
        var hand2 = List.of(
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX))
        );
        var hand3 = List.of(
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE))
        );
        List<List<PlayerCard>> board1 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));
        List<List<PlayerCard>> board2 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)), null)),
                new ArrayList<>(Arrays.asList(null, null, new PlayerCard(Card.from(Card.CardType.FOX)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));
        List<List<PlayerCard>> board3 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)), null)),
                new ArrayList<>(Arrays.asList(null, null, new PlayerCard(Card.from(Card.CardType.BEE)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));

        var player1 = new Player(playerID, "player1",
                hand1,null,
                board1,
                0);
        var player2 = new Player(playerID2, "player2",
                hand2,null,
                board2,
                0);
        var player3 = new Player(playerID3, "player3",
                hand3,null,
                board3,
                0);
        var game = new Game(gameID, List.of(player1,player2,player3), List.of(), 4);


        when(reactiveMongoTemplate.findOne(any(), eq(Game.class))).thenReturn(Mono.just(game));
        var res = gameService.updateGameStateIfTurnEnded(gameID).block();
        assertEquals(4,res.getTurn());
    }

    @Test
    void shouldProperlyUpdateGAmeStateWhenTurnEnds(){
        var hand1 = List.of(
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK))
        );
        var hand2 = List.of(
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX))
        );
        var hand3 = List.of(
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE))
        );
        List<List<PlayerCard>> board1 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.ELK)), null, null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));
        List<List<PlayerCard>> board2 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)), null)),
                new ArrayList<>(Arrays.asList(null, null, new PlayerCard(Card.from(Card.CardType.FOX)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));
        List<List<PlayerCard>> board3 = List.of(
                new ArrayList<>(Arrays.asList(null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)), null)),
                new ArrayList<>(Arrays.asList(null, null, new PlayerCard(Card.from(Card.CardType.BEE)), null)),
                new ArrayList<>(Arrays.asList(null, null, null, null)));

        var player1 = new Player(playerID, "player1",
                hand1,new SelectedMove(Card.CardType.ELK,new Slot(0,1),null,null),
                board1,
                0);
        var player2 = new Player(playerID2, "player2",
                hand2,new SelectedMove(Card.CardType.FOX,new Slot(0,1),null,null),
                board2,
                0);
        var player3 = new Player(playerID3, "player3",
                hand3,new SelectedMove(Card.CardType.BEE,new Slot(0,1),null,null),
                board3,
                0);
        var game = new Game(gameID, List.of(player1,player2,player3), List.of(), 4);


        when(reactiveMongoTemplate.findOne(any(), eq(Game.class))).thenReturn(Mono.just(game));
        when(reactiveMongoTemplate.findAndReplace(any(),any(),any(),eq(Game.COLLECTION_NAME))).thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(1)));
        var res = gameService.updateGameStateIfTurnEnded(gameID).block();
        assertEquals(5,res.getTurn());
        assertEquals(hand2.get(0),res.getPlayers().get(0).getCardsInHand().get(0));
    }
    @Test
    void shouldProperlyUpdateGameStateWhenHalfGameTurnEnds(){
        var hand1 = List.of(
                new PlayerCard(Card.from(Card.CardType.ELK)),
                new PlayerCard(Card.from(Card.CardType.ELK))
        );
        var hand2 = List.of(
                new PlayerCard(Card.from(Card.CardType.FOX)),
                new PlayerCard(Card.from(Card.CardType.FOX))
        );
        var hand3 = List.of(
                new PlayerCard(Card.from(Card.CardType.BEE)),
                new PlayerCard(Card.from(Card.CardType.BEE))
        );
        List<List<PlayerCard>> board1 = List.of(
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)),new PlayerCard(Card.from(Card.CardType.ELK)))),
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), null, null,null)),
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.ELK)), new PlayerCard(Card.from(Card.CardType.ELK)), null, null,null)),
                new ArrayList<>(Arrays.asList(null, null, null, null,null)));
        List<List<PlayerCard>> board2 = List.of(
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)),new PlayerCard(Card.from(Card.CardType.FOX)))),
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)), null, null,null)),
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.FOX)), new PlayerCard(Card.from(Card.CardType.FOX)), null, null,null)),
                new ArrayList<>(Arrays.asList(null, null, null, null,null)));
        List<List<PlayerCard>> board3 = List.of(
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)),new PlayerCard(Card.from(Card.CardType.BEE)))),
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)), null, null,null)),
                new ArrayList<>(Arrays.asList(new PlayerCard(Card.from(Card.CardType.BEE)), new PlayerCard(Card.from(Card.CardType.BEE)), null, null,null)),
                new ArrayList<>(Arrays.asList(null, null, null, null,null)));

        var player1 = new Player(playerID, "player1",
                hand1,new SelectedMove(Card.CardType.ELK,new Slot(3,0),null,null),
                board1,
                0);
        var player2 = new Player(playerID2, "player2",
                hand2,new SelectedMove(Card.CardType.FOX,new Slot(3,0),null,null),
                board2,
                0);
        var player3 = new Player(playerID3, "player3",
                hand3,new SelectedMove(Card.CardType.BEE,new Slot(3,0),null,null),
                board3,
                0);

        var game = new Game(gameID, List.of(player1,player2,player3), CardStack.initCardStack().stream().map(PlayerCard::new).toList().subList(33,130), 10);

        when(reactiveMongoTemplate.findOne(any(), eq(Game.class))).thenReturn(Mono.just(game));
        when(reactiveMongoTemplate.findAndReplace(any(),any(),any(),eq(Game.COLLECTION_NAME))).thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(1)));
        var res = gameService.updateGameStateIfTurnEnded(gameID).block();
        assertEquals(11,res.getTurn());
        assertTrue(res.getPlayers().stream().allMatch(it->it.getCardsInHand().size()==11));
        assertEquals(130-33-30, res.getCardStack().size());
        assertEquals(hand1.get(0),res.getPlayers().get(0).getCardsInHand().get(0));
        assertEquals(hand2.get(0),res.getPlayers().get(1).getCardsInHand().get(0));
        assertEquals(hand3.get(0),res.getPlayers().get(2).getCardsInHand().get(0));
    }
}
