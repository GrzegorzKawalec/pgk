package pl.gkawalec.pgk.database.migration;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.infrastructure.migration.PGKMigration;

@UtilityClass
public class MigrationEntityMapper {

    public MigrationEntity create(PGKMigration migration) {
        MigrationEntity result = new MigrationEntity();
        result.setVersion(migration.getVersion());
        result.setDescription(migration.getDescription());
        result.setExecutionTimeMs(migration.getExecutionTimeMs());
        return result;
    }


}
