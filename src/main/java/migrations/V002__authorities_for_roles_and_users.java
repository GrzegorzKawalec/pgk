package migrations;

import lombok.RequiredArgsConstructor;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntityMapper;
import pl.gkawalec.pgk.database.account.authority.AuthorityRepository;
import pl.gkawalec.pgk.infrastructure.migration.PGKMigration;

import java.util.Set;

@RequiredArgsConstructor
class V002__authorities_for_roles_and_users extends PGKMigration {

    private final AuthorityRepository authorityRepository;
    private final AuthorityEntityMapper authorityEntityMapper;

    @Override
    protected Set<String> requiredFlywayMigrationVersions() {
        return Set.of("1.003");
    }

    @Override
    protected void executeMigration() {
        addAuthority(Authority.ROLE_READ);
        addAuthority(Authority.ROLE_WRITE);
        addAuthority(Authority.USER_READ);
        addAuthority(Authority.USER_WRITE);
    }

    private void addAuthority(Authority authority) {
        if (authorityRepository.existsByAuthority(authority)) {
            return;
        }
        AuthorityEntity authorityEntity = authorityEntityMapper.create(authority);
        authorityRepository.save(authorityEntity);
    }

}
