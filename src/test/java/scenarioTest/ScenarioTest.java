package scenarioTest;


import com.eco.ecosystem.dto.GameDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URL;

public class ScenarioTest {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client;
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
