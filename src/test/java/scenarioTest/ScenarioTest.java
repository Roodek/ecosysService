package scenarioTest;


import com.eco.ecosystem.EcosystemApplication;
import com.eco.ecosystem.controllers.requestBodies.PlayerNameBody;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.CardStack;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import utils.AppUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

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

    Response createInitGameRequest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/new"))
                .post(RequestBody.create("",JSON)).build();
        return client.newCall(request).execute();
    }

    Response createJoinGameRequest(String gameID, PlayerNameBody requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder().url(new URL(getUrlRoot() + "/games/" + gameID + "/join"))
                .post(RequestBody.create(jsonString, JSON)).build();
        return client.newCall(request).execute();
    }

    void initGameWith3Players() throws IOException {
        var newGame = new GameDto(
                UUID.fromString(GAME_ID), List.of(
                new Player(
                        UUID.fromString(PLAYER1_ID),
                        PLAYER1,
                        List.of(),
                        List.of(List.of()), 0
                ),
                new Player(
                        UUID.fromString(PLAYER2_ID),
                        PLAYER2,
                        List.of(),
                        List.of(List.of()), 0
                ),
                new Player(
                        UUID.fromString(PLAYER3_ID),
                        PLAYER3,
                        List.of(),
                        List.of(List.of()), 0
                )
        ), CardStack.initCardStack().stream().map(PlayerCard::new).toList(),0);
        var dbRecord = AppUtils.gameDtoToEntity(newGame);
        reactiveMongoTemplate.save(dbRecord, "games").block();
    }

    Response createSaveGameRequest(GameDto requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder().url(new URL("localhost:8080/api/v1/games"))
                .post(RequestBody.create(jsonString, JSON)).build();
        return client.newCall(request).execute();
    }


}
