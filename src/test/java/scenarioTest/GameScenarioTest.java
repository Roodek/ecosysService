package scenarioTest;


import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
public class GameScenarioTest extends ScenarioTest {

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    void contextLoads(){}
    @Test
    void testInitGame() throws IOException {
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
        reactiveMongoTemplate.save(newGame);
        var response = createGetAllGamesRequest();
        assertEquals(200,response.code());
    }

}
