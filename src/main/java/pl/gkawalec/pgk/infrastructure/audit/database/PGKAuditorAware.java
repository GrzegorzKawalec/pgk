package pl.gkawalec.pgk.infrastructure.audit.database;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

@RequiredArgsConstructor
public class PGKAuditorAware implements AuditorAware<Auditor> {

    private final AuditorFetcher auditorFetcher;

    @NonNull
    @Override
    public Optional<Auditor> getCurrentAuditor() {
        return auditorFetcher.getAuditor();
    }

}
