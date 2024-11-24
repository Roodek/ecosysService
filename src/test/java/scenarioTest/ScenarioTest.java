package scenarioTest;


import com.eco.ecosystem.EcosystemApplication;
import com.eco.ecosystem.controllers.requestBodies.PlayerNameBody;
import com.eco.ecosystem.controllers.requestBodies.PutCardRequestBody;
import com.eco.ecosystem.controllers.requestBodies.PutRabbitCardAndSwapTwoRequestBody;
import com.eco.ecosystem.controllers.responseObjects.AvailableMovesResponse;
import com.eco.ecosystem.controllers.responseObjects.GameResponse;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.cards.Card;
import okhttp3.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.AppUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(classes = EcosystemApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScenarioTest {

    public static final String PLAYER1 = "player1";
    public static final String PLAYER2 = "player2";
    public static final String PLAYER3 = "player3";
    public static final String PLAYER1_ID = "9cf680e6-4f45-42bb-b1b6-d2f3816920de";
    public static final String PLAYER2_ID = "0fff5a7a-7dae-43bb-a9ec-a5867cc64d9f";
    public static final String PLAYER3_ID = "1bfdcbf5-9bd2-476f-866b-3ab901eda0a1";
    public static final String GAME_ID = "f368f16e-ce3f-47aa-aa5c-a8314accbe08";
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
    //sum: 130
    public static final List<Card> CARD_STACK = List.of(
            Card.from(Card.CardType.RIVER), Card.from(Card.CardType.MEADOW), Card.from(Card.CardType.RABBIT), Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.FOX), Card.from(Card.CardType.BEAR), Card.from(Card.CardType.MEADOW), Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.ELK), Card.from(Card.CardType.BEE), Card.from(Card.CardType.ELK), //player1 starting hand

            Card.from(Card.CardType.RIVER), Card.from(Card.CardType.RIVER), Card.from(Card.CardType.DRAGONFLY), Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.RABBIT), Card.from(Card.CardType.RABBIT), Card.from(Card.CardType.MEADOW), Card.from(Card.CardType.BEE),
            Card.from(Card.CardType.MEADOW), Card.from(Card.CardType.RIVER), Card.from(Card.CardType.BEAR), //player2 starting hand

            Card.from(Card.CardType.FISH), Card.from(Card.CardType.DRAGONFLY), Card.from(Card.CardType.ELK), Card.from(Card.CardType.ELK),
            Card.from(Card.CardType.ELK), Card.from(Card.CardType.ELK), Card.from(Card.CardType.RABBIT), Card.from(Card.CardType.ELK),
            Card.from(Card.CardType.BEE), Card.from(Card.CardType.EAGLE), Card.from(Card.CardType.FOX), //player3 starting hand
            Card.from(Card.CardType.WOLF),


            Card.from(Card.CardType.DRAGONFLY),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.BEE),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.EAGLE),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.RABBIT),
            Card.from(Card.CardType.ELK),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.BEE),
            Card.from(Card.CardType.BEE),
            Card.from(Card.CardType.EAGLE),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.DRAGONFLY),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.RABBIT),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.ELK),
            Card.from(Card.CardType.ELK),
            Card.from(Card.CardType.DRAGONFLY),
            Card.from(Card.CardType.ELK),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.RABBIT),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.EAGLE),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.RABBIT),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.BEE),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.EAGLE),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.DRAGONFLY),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.EAGLE),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.WOLF),
            Card.from(Card.CardType.BEE),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.EAGLE),
            Card.from(Card.CardType.DRAGONFLY),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.DRAGONFLY),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.ELK),
            Card.from(Card.CardType.BEAR),
            Card.from(Card.CardType.FOX),
            Card.from(Card.CardType.FISH),
            Card.from(Card.CardType.EAGLE),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.MEADOW),
            Card.from(Card.CardType.RIVER),
            Card.from(Card.CardType.MEADOW));

    @LocalServerPort
    private int port;
    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client;

    @Container
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:latest")
                    .withExposedPorts(27017);

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }

    @BeforeEach
    void setUp() {
        client = new OkHttpClient();
    }

    @AfterEach
    void finishWith() {
        reactiveMongoTemplate.getCollectionNames().flatMap(collection -> reactiveMongoTemplate.dropCollection(collection)).collectList().block();
    }

    private String getUrlRoot() {
        return "http://localhost:" + port + "/api/v1";
    }

    Response createGetAllGamesRequest() throws IOException {
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games")).build();
        return client.newCall(request).execute();
    }

    GameDto getGameWithID(UUID gameID) throws IOException {
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/" + gameID.toString())).build();
        var res = client.newCall(request).execute();
        assertEquals(200, res.code());
        return new ObjectMapper().readValue(res.body().string(), GameDto.class);
    }

    Response createInitGameRequest() throws IOException {
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/new"))
                .post(RequestBody.create("", JSON)).build();
        return client.newCall(request).execute();
    }

    Response createJoinGameRequest(String gameID, PlayerNameBody requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/" + gameID + "/join"))
                .post(RequestBody.create(jsonString, JSON)).build();
        return client.newCall(request).execute();
    }

    GameResponse createPutCardRequest(String gameID, String playerID, PutCardRequestBody requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/" + gameID + "/players/"+playerID+"/putCard"))
                .post(RequestBody.create(jsonString, JSON)).build();
        var res = client.newCall(request).execute();
        assertEquals(200,res.code());
        return new ObjectMapper().readValue(res.body().string(), GameResponse.class);
    }
    GameResponse createSwapMoveRequest(String gameID, String playerID, PutRabbitCardAndSwapTwoRequestBody requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/" + gameID + "/players/"+playerID+"/swapMove"))
                .post(RequestBody.create(jsonString, JSON)).build();
        var res = client.newCall(request).execute();
        assertEquals(200,res.code());
        return new ObjectMapper().readValue(res.body().string(), GameResponse.class);
    }

    Response createStartGameRequest(String gameID) throws IOException {
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/" + gameID + "/start"))
                .post(RequestBody.create("", JSON)).build();
        var res = client.newCall(request).execute();
        assertEquals(200, res.code());
        return res;
    }

    AvailableMovesResponse getAvailableMoves(String gameID, String playerID) throws IOException {
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/" + gameID+"/players/"+playerID+"/availableMoves")).build();
        var res = client.newCall(request).execute();
        assertEquals(200, res.code());
        return new ObjectMapper().readValue(res.body().string(), AvailableMovesResponse.class);
    }

    void initGameWith3Players() throws IOException {
        var newGame = new GameDto(
                UUID.fromString(GAME_ID), List.of(
                new Player(
                        UUID.fromString(PLAYER1_ID),
                        PLAYER1,
                        CARD_STACK.subList(0,11).stream().map(PlayerCard::new).toList(),
                        null,
                        List.of(List.of()), 0
                ),
                new Player(
                        UUID.fromString(PLAYER2_ID),
                        PLAYER2,
                        CARD_STACK.subList(11,22).stream().map(PlayerCard::new).toList(), null,
                        List.of(List.of()), 0
                ),
                new Player(
                        UUID.fromString(PLAYER3_ID),
                        PLAYER3,
                        CARD_STACK.subList(22,33).stream().map(PlayerCard::new).toList(), null,
                        List.of(List.of()), 0
                )
        ), CARD_STACK.subList(33,CARD_STACK.size()).stream().map(PlayerCard::new).toList(), 1);
        var dbRecord = AppUtils.gameDtoToEntity(newGame);

        reactiveMongoTemplate.save(dbRecord, "games").block();
        var createdGame = getGameWithID(UUID.fromString(GAME_ID));
        assertEquals(3,createdGame.getPlayers().size());
        assertTrue(createdGame.getPlayers().stream().allMatch(player -> player.getCardsInHand().size()==11));
        assertEquals(97,createdGame.getCardStack().size());
    }

    Response createSaveGameRequest(GameDto requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder().url(new URL("localhost:8080/api/v1/games"))
                .post(RequestBody.create(jsonString, JSON)).build();
        return client.newCall(request).execute();
    }


}
