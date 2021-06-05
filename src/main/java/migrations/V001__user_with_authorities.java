package migrations;

import lombok.RequiredArgsConstructor;
import pl.gkawalec.pgk.api.dto.account.UserDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.user.AnonymousUserAccessor;
import pl.gkawalec.pgk.common.user.SystemUserAccessor;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntityMapper;
import pl.gkawalec.pgk.database.account.authority.AuthorityRepository;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntityMapper;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.database.account.user.*;
import pl.gkawalec.pgk.infrastructure.migration.PGKMigration;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
class V001__user_with_authorities extends PGKMigration {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    private final AuthorityEntityMapper authorityEntityMapper;

    @Override
    protected Set<String> requiredFlywayMigrationVersions() {
        return Set.of("1.003");
    }

    @Override
    protected void executeMigration() {
        insertSystemUser();
        insertAnonymousUser();
        AuthorityEntity adminAuthorityEntity = insertAdminAuthority();
        RoleEntity adminRoleEntity = insertRoleAdmin(adminAuthorityEntity);
        UserEntity adminEntity = insertAdminUser();
        insertAdminUserCredentials(adminEntity, adminRoleEntity);
    }

    private void insertSystemUser() {
        insertInternalUser(SystemUserAccessor.IDENTIFIER);
    }

    private void insertAnonymousUser() {
        insertInternalUser(AnonymousUserAccessor.IDENTIFIER);
    }

    private void insertInternalUser(String identifier) {
        if (userRepository.existsByEmail(identifier)) return;
        UserDTO dto = UserDTO.builder()
                .email(identifier)
                .firstName(identifier)
                .lastName(identifier)
                .build();
        UserEntity entity = UserEntityMapper.create(dto);
        userRepository.save(entity);
    }

    private AuthorityEntity insertAdminAuthority() {
        Authority adminAuthority = Authority.ADMIN;
        AuthorityEntity adminAuthorityEntity = authorityRepository.findByAuthority(adminAuthority);
        if (Objects.nonNull(adminAuthorityEntity)) {
            return adminAuthorityEntity;
        }
        adminAuthorityEntity = authorityEntityMapper.create(adminAuthority);
        return authorityRepository.save(adminAuthorityEntity);
    }

    private RoleEntity insertRoleAdmin(AuthorityEntity adminAuthorityEntity) {
        String roleName = "Administrator";
        RoleEntity adminRoleEntity = roleRepository.findByName(roleName);
        if (Objects.nonNull(adminRoleEntity)) {
            return adminRoleEntity;
        }
        String description = "Administrator aplikacji";
        adminRoleEntity = RoleEntityMapper.create(roleName, description, adminAuthorityEntity);
        return roleRepository.save(adminRoleEntity);
    }

    private UserEntity insertAdminUser() {
        String adminEmail = "gkawalecPGK@gmail.com";
        UserEntity adminEntity = userRepository.findByEmail(adminEmail);
        if (Objects.nonNull(adminEntity)) {
            return adminEntity;
        }
        UserDTO dto = UserDTO.builder()
                .email(adminEmail)
                .firstName("Grzegorz")
                .lastName("Kawalec")
                .phoneNumber("+48 123 456 789")
                .description("Twórca oraz administrator aplikacji")
                .build();
        adminEntity = UserEntityMapper.create(dto);
        return userRepository.save(adminEntity);
    }

    private void insertAdminUserCredentials(UserEntity adminEntity, RoleEntity adminRoleEntity) {
        if (userCredentialsRepository.existsByEmail(adminEntity.getEmail())) return;
        String password = "pgk_gk91";
        UserCredentialsEntity credentialsEntity = UserCredentialsEntityMapper.create(adminEntity, adminRoleEntity, password);
        userCredentialsRepository.save(credentialsEntity);
    }

}
