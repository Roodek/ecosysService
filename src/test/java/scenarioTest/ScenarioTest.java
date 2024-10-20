package scenarioTest;


import com.eco.ecosystem.EcosystemApplication;
import com.eco.ecosystem.dto.GameDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URL;
@Testcontainers
@SpringBootTest(classes = EcosystemApplication.class)
public class ScenarioTest {

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
    void setUp(){
        client = new OkHttpClient();
    }

    Response createGetAllGamesRequest() throws IOException {
        Request request = new Request.Builder().url(new URL("localhost:8080/api/v1/games")).build();
        return client.newCall(request).execute();
    }

    Response createSaveGameRequest(GameDto requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder().url(new URL("localhost:8080/api/v1/games"))
                .post(RequestBody.create(jsonString,JSON)).build();
        return client.newCall(request).execute();
    }


}
