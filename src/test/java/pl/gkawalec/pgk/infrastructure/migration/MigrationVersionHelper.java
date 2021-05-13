package pl.gkawalec.pgk.infrastructure.migration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.database.migration.MigrationRepository;

@Component
@RequiredArgsConstructor
class MigrationVersionHelper {

    private final MigrationRepository repository;

    int getFirstAvailableVersion() {
        return repository.findFirstByOrderByVersionDesc()
                .map(migration -> migration.getVersion() + 1)
                .orElse(1);
    }

}
