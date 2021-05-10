package pl.gkawalec.pgk.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pl.gkawalec.pgk.infrastructure.audit.database.Auditor;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditorFetcher;
import pl.gkawalec.pgk.infrastructure.audit.database.PGKAuditorAware;

@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

    @Bean
    AuditorAware<Auditor> auditorAware(AuditorFetcher auditorFetcher) {
        return new PGKAuditorAware(auditorFetcher);
    }

}
