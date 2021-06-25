package pl.gkawalec.pgk.test.util;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.authority.AuthorityRepository;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntityMapper;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.database.account.user.*;
import pl.gkawalec.pgk.test.annotation.PGKTestProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@PGKTestProfiles
public class TestUserUtil {

    private static final Set<Authority> AUTHORITIES = Set.of(Authority.values());

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    public void createUserExcludedAuthority(String email, String password, Authority excludedAuthority) {
        createUserExcludedAuthorities(email, password, Set.of(excludedAuthority));
    }

    public void createUserExcludedAuthorities(String email, String password, Set<Authority> excludedAuthorities) {
        Set<Object> excludedAuthoritiesWithAdmin = new HashSet<>(excludedAuthorities);
        excludedAuthoritiesWithAdmin.add(Authority.ADMIN);
        Sets.SetView<Authority> authorities = Sets.difference(AUTHORITIES, excludedAuthoritiesWithAdmin);
        createUserWithAuthorities(email, password, authorities);
    }

    public UserEntity createUserWithAuthority(String email, String password, Authority authority) {
        return createUserWithAuthorities(email, password, Set.of(authority));
    }

    public UserEntity createUserWithAuthorities(String email, String password, Set<Authority> authorities) {
        if (userCredentialsRepository.existsByEmail(email)) {
            throw new UnsupportedOperationException("Cannot create user because email already exists, email: " + email);
        }
        List<AuthorityEntity> authorityEntities = authorityRepository.findAllByAuthorityIn(authorities);
        RoleEntity roleEntity = insertRole(email, authorityEntities);
        UserEntity userEntity = insertUser(email);
        insertCredentials(roleEntity, userEntity, password);
        return userEntity;
    }

    public UserEntity insertUser(String email) {
        UserDTO dto = UserDTO.builder()
                .email(email)
                .firstName(email)
                .lastName(email)
                .build();
        UserEntity userEntity = UserEntityMapper.create(dto);
        return userRepository.saveAndFlush(userEntity);
    }

    private RoleEntity insertRole(String roleName, List<AuthorityEntity> authorityEntities) {
        RoleEntity roleEntity = roleRepository.findByName(roleName);
        if (Objects.nonNull(roleEntity)) {
            throw new UnsupportedOperationException("Cannot create role because roleName already exists, roleName: " + roleName);
        }
        roleEntity = RoleEntityMapper.create(roleName, null, authorityEntities);
        return roleRepository.saveAndFlush(roleEntity);
    }

    private void insertCredentials(RoleEntity roleEntity, UserEntity userEntity, String password) {
        UserCredentialsEntity credentialsEntity = UserCredentialsEntityMapper.create(userEntity, roleEntity, password);
        userCredentialsRepository.saveAndFlush(credentialsEntity);
    }

}
