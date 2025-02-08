package scenarioTest;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.services.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.AppUtils;
import utils.Timestamp;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearOldGamesScenarioTest extends ScenarioTest{

    UUID GAME2_ID = UUID.fromString("93800992-8cb0-49a7-8cee-9d64c1f50246");
    @Autowired
    GameService gameService;
    @Test
    void testClearOldGames() throws IOException {
        initGameWith3Players();
        var newGame = new GameDto(
                GAME2_ID, List.of(
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
        ), CARD_STACK.subList(33,CARD_STACK.size()).stream().map(PlayerCard::new).toList(), 1, new Timestamp(Instant.now().minus(3, ChronoUnit.HOURS)));
        var dbRecord = AppUtils.gameDtoToEntity(newGame);

        reactiveMongoTemplate.save(dbRecord, "games").block();
        var allGames= createGetAllGamesRequest();
        assertEquals(2,allGames.size());

        gameService.scheduleDeletingOldGames();

        allGames = createGetAllGamesRequest();
        assertEquals(1,allGames.size());

    }
}
