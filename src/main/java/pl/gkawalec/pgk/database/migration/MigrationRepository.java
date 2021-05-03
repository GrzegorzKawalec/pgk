package pl.gkawalec.pgk.database.migration;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

import java.util.Optional;

public interface MigrationRepository extends BaseNumberIDRepository<MigrationEntity> {

    Optional<MigrationEntity> findByVersion(int version);

    Optional<MigrationEntity> findFirstByOrderByVersionDesc();

}
