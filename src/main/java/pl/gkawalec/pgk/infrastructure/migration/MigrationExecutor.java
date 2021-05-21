package pl.gkawalec.pgk.infrastructure.migration;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.database.migration.FlywayMigrationsRepository;
import pl.gkawalec.pgk.database.migration.MigrationEntity;
import pl.gkawalec.pgk.database.migration.MigrationRepository;
import pl.gkawalec.pgk.common.exception.MigrationException;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
class MigrationExecutor {

    private final MigrationRepository repository;
    private final FlywayMigrationsRepository flywayRepository;

    @Transactional
    public void execute(PGKMigration migration) {
        Optional<MigrationEntity> existMigration = repository.findByVersion(migration.getVersion());
        if (existMigration.isPresent()) {
            migration.migrationSkipped();
            checkIfDescriptionIsTheSame(migration, existMigration.get());
            return;
        }

        validate(migration);
        migration.execute();
    }

    private void checkIfDescriptionIsTheSame(PGKMigration migration, MigrationEntity entity) {
        if (!Objects.equals(entity.getDescription(), migration.getDescription())) {
            String errorMessage = "Migration '" + migration.getVersion() + "' has a different description than in the database." +
                    " Description in migration: " + migration.getDescription() + "." +
                    " Description in database: " + entity.getDescription() + ".";
            throw new MigrationException(errorMessage);
        }
    }

    private void validate(PGKMigration migration) {
        try {
            repository.findFirstByOrderByVersionDesc()
                    .ifPresent(entity -> checkDifferenceBetweenVersion(entity, migration));
            checkExistsRequiredFlywayMigrationVersions(migration);
        } catch (Exception ex) {
            migration.migrationSkipped();
            throw ex;
        }
    }

    private void checkDifferenceBetweenVersion(MigrationEntity entity, PGKMigration migration) {
        int versionDifference = migration.getVersion() - entity.getVersion();
        if (versionDifference != 1) {
            String errorMessage = "The version difference between the last migration and the current one cannot be different than 1." +
                    " The last migration in the database: " + entity.getVersion() + "." +
                    " The current version: " + migration.getVersion() + ".";
            throw new MigrationException(errorMessage);
        }
    }

    private void checkExistsRequiredFlywayMigrationVersions(PGKMigration migration) {
        Set<String> versions = migration.requiredFlywayMigrationVersions();
        Set<String> existsVersions = flywayRepository.getExistsVersions(versions);

        if (versions.size() == existsVersions.size()) return;

        Sets.SetView<String> difference = Sets.difference(versions, existsVersions);
        String missingFlywayMigrations = String.join(",", difference);
        String errorMessage = "Required Flyway migration versions not found." +
                " Migration version: " + migration.getVersion() + "." +
                " Missing Flyway migration: " + missingFlywayMigrations + ".";
        throw new MigrationException(errorMessage);
    }

}
