package migrations;

import lombok.RequiredArgsConstructor;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntityMapper;
import pl.gkawalec.pgk.database.account.authority.AuthorityRepository;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntityMapper;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.infrastructure.migration.PGKMigration;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
class V003__guest_authority_and_role extends PGKMigration {

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;
    private final AuthorityEntityMapper authorityEntityMapper;

    @Override
    protected Set<String> requiredFlywayMigrationVersions() {
        return Set.of("1.003");
    }

    @Override
    protected void executeMigration() {
        AuthorityEntity guestAuthorityEntity = insertGuestAuthority();
        insertGuestRole(guestAuthorityEntity);
    }

    private AuthorityEntity insertGuestAuthority() {
        Authority guestAuthority = Authority.GUEST;
        AuthorityEntity guestAuthorityEntity = authorityRepository.findByAuthority(guestAuthority);
        if (Objects.nonNull(guestAuthorityEntity)) {
            return guestAuthorityEntity;
        }
        guestAuthorityEntity = authorityEntityMapper.create(guestAuthority);
        return authorityRepository.save(guestAuthorityEntity);
    }

    private void insertGuestRole(AuthorityEntity guestAuthorityEntity) {
        String roleName = "Gość";
        RoleEntity guestRoleEntity = roleRepository.findByName(roleName);
        if (Objects.nonNull(guestRoleEntity)) {
            return;
        }
        String description = "Gość ma przyznane minimalne uprawnienia w aplikacji";
        guestRoleEntity = RoleEntityMapper.create(roleName, description, guestAuthorityEntity);
        roleRepository.save(guestRoleEntity);
    }

}
