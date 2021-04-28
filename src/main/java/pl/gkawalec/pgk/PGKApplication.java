package pl.gkawalec.pgk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class PGKApplication {

    public static void main(String[] args) {
        SpringApplication.run(PGKApplication.class, args);
    }

}
