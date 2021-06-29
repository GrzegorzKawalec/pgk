package migrations;

import lombok.RequiredArgsConstructor;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntityMapper;
import pl.gkawalec.pgk.database.account.authority.AuthorityRepository;
import pl.gkawalec.pgk.infrastructure.migration.PGKMigration;

import java.util.Set;

@RequiredArgsConstructor
public class V004__authorities_for_projects_and_legal_acts extends PGKMigration {

    private final AuthorityRepository authorityRepository;
    private final AuthorityEntityMapper authorityEntityMapper;

    @Override
    protected Set<String> requiredFlywayMigrationVersions() {
        return Set.of("1.003");
    }

    @Override
    protected void executeMigration() {
        addAuthority(Authority.PROJECT_READ);
        addAuthority(Authority.PROJECT_WRITE);
        addAuthority(Authority.LEGAL_ACTS_READ);
        addAuthority(Authority.LEGAL_ACTS_WRITE);
    }

    private void addAuthority(Authority authority) {
        if (authorityRepository.existsByAuthority(authority)) {
            return;
        }
        AuthorityEntity authorityEntity = authorityEntityMapper.create(authority);
        authorityRepository.save(authorityEntity);
    }

}
