package pl.gkawalec.pgk.database.migration;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

import java.util.Collection;
import java.util.Set;

public interface FlywayMigrationsRepository extends BaseNumberIDRepository<FlywayMigrationEntity> {

    @Query("select f.version" +
            " from FlywayMigrationEntity f" +
            " where f.version in (:versions)")
    Set<String> getExistsVersions(@Param("versions") Collection<String> versions);

    boolean existsByVersion(String version);

}
