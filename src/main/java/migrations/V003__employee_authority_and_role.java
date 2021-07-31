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
class V003__employee_authority_and_role extends PGKMigration {

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;
    private final AuthorityEntityMapper authorityEntityMapper;

    @Override
    protected Set<String> requiredFlywayMigrationVersions() {
        return Set.of("1.003");
    }

    @Override
    protected void executeMigration() {
        AuthorityEntity employeeAuthorityEntity = insertEmployeeAuthority();
        insertEmployeeRole(employeeAuthorityEntity);
    }

    private AuthorityEntity insertEmployeeAuthority() {
        Authority employeeAuthority = Authority.EMPLOYEE;
        AuthorityEntity employeeAuthorityEntity = authorityRepository.findByAuthority(employeeAuthority);
        if (Objects.nonNull(employeeAuthorityEntity)) {
            return employeeAuthorityEntity;
        }
        employeeAuthorityEntity = authorityEntityMapper.create(employeeAuthority);
        return authorityRepository.save(employeeAuthorityEntity);
    }

    private void insertEmployeeRole(AuthorityEntity employeeAuthorityEntity) {
        String roleName = "Pracownik";
        RoleEntity employeeRoleEntity = roleRepository.findByName(roleName);
        if (Objects.nonNull(employeeRoleEntity)) {
            return;
        }
        String description = "Pracownik ma przyznane niezbędne uprawnienia w aplikacji, aby być uczestnikiem projektów";
        employeeRoleEntity = RoleEntityMapper.create(roleName, description, employeeAuthorityEntity);
        roleRepository.save(employeeRoleEntity);
    }

}
