package pl.gkawalec.pgk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.gkawalec.pgk.testconfig.annotation.PGKSpringBootTest;

@PGKSpringBootTest
class PGKApplicationTest {

    @Test
    @DisplayName("Test Spring Boot context loads")
    void contextLoads() {
    }

}
