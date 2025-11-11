package scenarioTest;


import com.eco.ecosystem.controllers.requestBodies.PlayerNameBody;
import com.eco.ecosystem.controllers.requestBodies.PutCardRequestBody;
import com.eco.ecosystem.controllers.requestBodies.PutRabbitCardAndSwapTwoRequestBody;
import com.eco.ecosystem.controllers.responseObjects.GameResponse;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Game;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.entities.SelectedMove;
import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.Card;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import utils.AppUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameScenarioTest extends ScenarioTest {

    @Test
    void contextLoads(){}
    @Test
    void testGetAllNonStartedGames() throws IOException {
        List<List<PlayerCard>> board = List.of(
                Arrays.asList(null,null,null),
                Arrays.asList(null,new PlayerCard("MEADOW"),null),
                Arrays.asList(null,null,null)
        );
        var newGame = new GameDto(
                UUID.randomUUID(),
                List.of(
                        new Player(
                                UUID.randomUUID(),
                                "player1",
                                List.of(new PlayerCard("ELK"), new PlayerCard("RIVER")),null,
                                board,0
                        )),
                List.of(new PlayerCard("BEE"), new PlayerCard("ELK")),3);
        var newGame0turn = new GameDto(
                UUID.randomUUID(),
                List.of(
                        new Player(
                                UUID.randomUUID(),
                                "player1",
                                List.of(new PlayerCard("ELK"), new PlayerCard("RIVER")),null,
                                board,0
                        )),
                List.of(new PlayerCard("BEE"), new PlayerCard("ELK")),0);
        reactiveMongoTemplate.save(AppUtils.gameDtoToEntity(newGame),"games").block();
        reactiveMongoTemplate.save(AppUtils.gameDtoToEntity(newGame0turn),"games").block();

        assertEquals(2, reactiveMongoTemplate.findAll(Game.class, "games")
                .collectList()
                .block().size());

        var response = createGetAllNonStartedGamesRequest();
        ArrayList<GameDto> resBody = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<GameDto>>(){});
        assertEquals(200,response.code());
        assertEquals(1,resBody.size());
        assertEquals(0,resBody.get(0).getTurn());

    }

    @Test
    void testInitGame() throws IOException {
        var playerNameBody = new PlayerNameBody("player1");
        var response = createInitGameRequest();
        assertEquals(200,response.code());
        var allGamesResponse = createGetAllNonStartedGamesRequest();
        ArrayList<GameDto> allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        var game = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(0, game.getCardStack().size());
    }

    @Test
    void testInitGameAndJoin() throws IOException {
        var playerNameBody1 = new PlayerNameBody("player1");
        var playerNameBody2 = new PlayerNameBody("player2");
        var createInitGameResponse = createInitGameRequest();
        assertEquals(200,createInitGameResponse.code());
        var allGamesResponse = createGetAllNonStartedGamesRequest();
        ArrayList<GameDto> allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        var game = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(0,game.getPlayers().size());
        assertEquals(0, game.getCardStack().size());

        var gameId = new ObjectMapper().readValue(createInitGameResponse.body().string(), String.class);
        var joinGameResponse1 = createJoinGameRequest(gameId,playerNameBody1);
        assertEquals(200,joinGameResponse1.code());
        assertNotNull(UUID.fromString( new ObjectMapper().readValue(joinGameResponse1.body().string(), String.class)));

        var joinGameResponse2 = createJoinGameRequest(gameId,playerNameBody2);
        assertEquals(200,joinGameResponse2.code());
        assertNotNull(UUID.fromString( new ObjectMapper().readValue(joinGameResponse2.body().string(), String.class)));

        allGamesResponse = createGetAllNonStartedGamesRequest();
        allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        game = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(2,game.getPlayers().size());
        assertTrue(game.getPlayers().stream().map(Player::getName).anyMatch(name->name.equals("player1")));
        assertTrue(game.getPlayers().stream().map(Player::getName).anyMatch(name->name.equals("player2")));
    }

    @Test
    void testStartGame() throws IOException {
        var playerNameBody1 = new PlayerNameBody("player1");
        var playerNameBody2 = new PlayerNameBody("player2");
        var playerNameBody3 = new PlayerNameBody("player3");
        var createInitGameResponse = createInitGameRequest();
        assertEquals(200,createInitGameResponse.code());
        var allGamesResponse = createGetAllNonStartedGamesRequest();
        ArrayList<GameDto> allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        var game1 = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(0,game1.getPlayers().size());
        assertEquals(0, game1.getCardStack().size());

        var gameId = new ObjectMapper().readValue(createInitGameResponse.body().string(), String.class);
        var joinGameResponse1 = createJoinGameRequest(gameId,playerNameBody1);
        assertEquals(200,joinGameResponse1.code());
        assertNotNull(UUID.fromString( new ObjectMapper().readValue(joinGameResponse1.body().string(), String.class)));

        var joinGameResponse2 = createJoinGameRequest(gameId,playerNameBody2);
        assertEquals(200,joinGameResponse2.code());
        assertNotNull(UUID.fromString( new ObjectMapper().readValue(joinGameResponse2.body().string(), String.class)));

        var joinGameResponse3 = createJoinGameRequest(gameId,playerNameBody3);
        assertEquals(200,joinGameResponse3.code());
        assertNotNull(UUID.fromString( new ObjectMapper().readValue(joinGameResponse3.body().string(), String.class)));

        allGamesResponse = createGetAllNonStartedGamesRequest();
        allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        game1 = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(3,game1.getPlayers().size());
        assertTrue(game1.getPlayers().stream().map(Player::getName).anyMatch(name->name.equals("player1")));
        assertTrue(game1.getPlayers().stream().map(Player::getName).anyMatch(name->name.equals("player2")));
        assertTrue(game1.getPlayers().stream().map(Player::getName).anyMatch(name->name.equals("player3")));

        var res = createStartGameRequest(gameId);
        var game = getGameWithID(UUID.fromString(gameId));
        game.getPlayers().stream().allMatch(player -> player.getCardsInHand().size()==11);
    }

    @Test
    void testWholeGame() throws IOException {
        initGameWith3Players();

        assertEquals(List.of(new Slot(0,0)),getAvailableMoves(GAME_ID,PLAYER1_ID).getAvailableSlots());
        assertEquals(List.of(new Slot(0,0)),getAvailableMoves(GAME_ID,PLAYER2_ID).getAvailableSlots());
        assertEquals(List.of(new Slot(0,0)),getAvailableMoves(GAME_ID,PLAYER3_ID).getAvailableSlots());

        var board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.BEE,new Slot(0,0)));
        var board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.BEE,new Slot(0,0)));
        var board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.BEE,new Slot(0,0)));
        verifyFirstSelection(board1res, board2res, board3res);


        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(0,1)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.FISH,new Slot(0,1)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(0,1)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.DRAGONFLY,new Slot(0,1)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(0,1)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(0,1)));

        board1res = createSwapMoveRequest(GAME_ID,PLAYER1_ID,new PutRabbitCardAndSwapTwoRequestBody(new Slot(0,1),new Slot(0,1),new Slot(1,1)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.DRAGONFLY,new Slot(0,1)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.ELK,new Slot(0,1)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(1,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.ELK,new Slot(1,0)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.WOLF,new Slot(1,0)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.ELK,new Slot(1,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.FOX,new Slot(1,0)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(1,0)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.BEAR,new Slot(1,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(1,0)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.ELK,new Slot(1,0)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(1,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.ELK,new Slot(1,0)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(1,0)));

        var gameAfterReachingMaxDimensions = getGameWithID(UUID.fromString(GAME_ID));
        assertEquals(4,gameAfterReachingMaxDimensions.getPlayers().get(0).getBoard().size());
        assertEquals(5,gameAfterReachingMaxDimensions.getPlayers().get(0).getBoard().get(0).size());

        board1res = createSwapMoveRequest(GAME_ID,PLAYER1_ID,new PutRabbitCardAndSwapTwoRequestBody(new Slot(1,1),new Slot(1,1),new Slot(0,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.WOLF,new Slot(1,1)));
        //swap elk with river
        board3res = createSwapMoveRequest(GAME_ID,PLAYER3_ID,new PutRabbitCardAndSwapTwoRequestBody(new Slot(1,1),new Slot(0,2),new Slot(2,4)));

        //verify swapped cards
        var gameAfterRabbitSwapInFirstAndThirdPlayer = getGameWithID(UUID.fromString(GAME_ID));
        assertEquals(Card.CardType.RIVER.toString(),gameAfterRabbitSwapInFirstAndThirdPlayer.getPlayers().get(0).getBoard().get(1).get(1).getCardType());
        assertEquals(Card.CardType.RABBIT.toString(),gameAfterRabbitSwapInFirstAndThirdPlayer.getPlayers().get(0).getBoard().get(0).get(0).getCardType());
        assertEquals(Card.CardType.RIVER.toString(),gameAfterRabbitSwapInFirstAndThirdPlayer.getPlayers().get(2).getBoard().get(0).get(2).getCardType());
        assertEquals(Card.CardType.MEADOW.toString(),gameAfterRabbitSwapInFirstAndThirdPlayer.getPlayers().get(2).getBoard().get(2).get(4).getCardType());

        gameAfterRabbitSwapInFirstAndThirdPlayer.getPlayers().forEach(Player::printBoard);

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.ELK,new Slot(1,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.RABBIT,new Slot(1,0)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.EAGLE,new Slot(1,0)));

        var gameAfter10Turn = getGameWithID(UUID.fromString(GAME_ID));
        assertTrue(gameAfter10Turn.getPlayers().stream().allMatch(player -> player.getCardsInHand().size()==11));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.ELK,new Slot(1,2)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.BEAR,new Slot(1,2)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.FOX,new Slot(1,2)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.WOLF,new Slot(2,1)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.WOLF,new Slot(2,1)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.BEAR,new Slot(2,1)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(2,2)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.BEE,new Slot(2,2)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.DRAGONFLY,new Slot(2,2)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(2,3)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.BEE,new Slot(2,3)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.BEE,new Slot(2,3)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.EAGLE,new Slot(3,3)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.FOX,new Slot(3,3)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(3,3)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.EAGLE,new Slot(1,3)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.FOX,new Slot(1,3)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(1,3)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(2,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.WOLF,new Slot(2,0)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(2,0)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(3,0)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.FISH,new Slot(3,0)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.FISH,new Slot(3,0)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(3,1)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.MEADOW,new Slot(3,1)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.RIVER,new Slot(3,1)));

        board1res = createPutCardRequest(GAME_ID,PLAYER1_ID,new PutCardRequestBody(Card.CardType.BEAR,new Slot(3,2)));
        board2res = createPutCardRequest(GAME_ID,PLAYER2_ID,new PutCardRequestBody(Card.CardType.RABBIT,new Slot(3,2)));
        board3res = createPutCardRequest(GAME_ID,PLAYER3_ID,new PutCardRequestBody(Card.CardType.DRAGONFLY,new Slot(3,2)));

        var gameAfterEnd = getGameWithID(UUID.fromString(GAME_ID));
        gameAfterEnd.getPlayers().forEach(Player::printBoard);
        //List.of(getAvailableMoves(GAME_ID,PLAYER1_ID),getAvailableMoves(GAME_ID,PLAYER2_ID),getAvailableMoves(GAME_ID,PLAYER3_ID));

    }

    private void verifyFirstSelection(GameResponse board1res, GameResponse board2res, GameResponse board3res) {
        verifySelectedMove(new SelectedMove(Card.CardType.BEE,new Slot(0,0),null,null),
                board1res.getPlayers().stream()
                .filter(player -> player.getId().equals(UUID.fromString(PLAYER1_ID)))
                .toList().get(0).getSelectedMove());
        verifySelectedMove(new SelectedMove(Card.CardType.BEE,new Slot(0,0),null,null),
                board2res.getPlayers().stream()
                        .filter(player -> player.getId().equals(UUID.fromString(PLAYER2_ID)))
                        .toList().get(0).getSelectedMove());
//        verifySelectedMove(new SelectedMove(Card.CardType.BEE,new Slot(0,0),null,null),
//                board3res.getPlayers().stream()
//                        .filter(player -> player.getId().equals(UUID.fromString(PLAYER3_ID)))
//                        .toList().get(0).getSelectedMove());
    }

    private void verifySelectedMove(SelectedMove expected,SelectedMove actual){
        assertEquals(expected.getSelectedCard(),actual.getSelectedCard());
        assertEquals(expected.getSelectedSlot(),actual.getSelectedSlot());
    }
}
