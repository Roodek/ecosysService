package scenarioTest;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
public class ConfigurationTest {

    @Test
    void contextLoads(){}
}
