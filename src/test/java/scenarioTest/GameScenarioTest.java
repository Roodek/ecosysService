package scenarioTest;


import com.eco.ecosystem.controllers.requestBodies.PlayerNameBody;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
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
    void testGetAllGames() throws IOException {
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
                                List.of(new PlayerCard("ELK"), new PlayerCard("RIVER")),
                                board,0
                        )),
                List.of(new PlayerCard("BEE"), new PlayerCard("ELK")));
        reactiveMongoTemplate.save(AppUtils.gameDtoToEntity(newGame),"games").block();
        var response = createGetAllGamesRequest();
        ArrayList<GameDto> resBody = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<GameDto>>(){});
        assertEquals(200,response.code());
        assertEquals(1,resBody.size());

    }

    @Test
    void testInitGame() throws IOException {
        var playerNameBody = new PlayerNameBody("player1");
        var response = createInitGameRequest(playerNameBody);
        assertEquals(200,response.code());
        var allGamesResponse = createGetAllGamesRequest();
        ArrayList<GameDto> allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        var game = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(1,game.getPlayers().size());
        assertEquals("player1",game.getPlayers().get(0).getName());
        assertEquals(130, game.getCardStack().size());
    }

    @Test
    void testInitGameAndJoin() throws IOException {
        var playerNameBody1 = new PlayerNameBody("player1");
        var playerNameBody2 = new PlayerNameBody("player2");
        var createInitGameResponse = createInitGameRequest(playerNameBody1);
        assertEquals(200,createInitGameResponse.code());
        var allGamesResponse = createGetAllGamesRequest();
        ArrayList<GameDto> allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        var game = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(1,game.getPlayers().size());
        assertEquals("player1",game.getPlayers().get(0).getName());
        assertEquals(130, game.getCardStack().size());
        var gameId = new ObjectMapper().readValue(createInitGameResponse.body().string(), String.class);
        var joinGameResponse = createJoinGameRequest(gameId,playerNameBody2);
        assertEquals(200,joinGameResponse.code());
        assertNotNull(UUID.fromString( new ObjectMapper().readValue(joinGameResponse.body().string(), String.class)));

        allGamesResponse = createGetAllGamesRequest();
        allGames = new ObjectMapper().readValue(allGamesResponse.body().string(), new TypeReference<List<GameDto>>(){});
        game = allGames.get(0);
        assertEquals(200,allGamesResponse.code());
        assertEquals(1,allGames.size());
        assertEquals(2,game.getPlayers().size());
        assertTrue(game.getPlayers().stream().map(Player::getName).anyMatch(name->name.equals("player1")));
        assertTrue(game.getPlayers().stream().map(Player::getName).anyMatch(name->name.equals("player2")));
    }

}
