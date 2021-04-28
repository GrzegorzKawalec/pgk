package pl.gkawalec.pgk.testconfig;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.gkawalec.pgk.testconfig.annotation.PGKTestProfiles;

@Configuration
@PGKTestProfiles
public class FlywayTestConfig {

    @Bean
    public FlywayMigrationStrategy clean() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }

}
