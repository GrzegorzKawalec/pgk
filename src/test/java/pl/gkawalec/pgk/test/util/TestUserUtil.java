package pl.gkawalec.pgk.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.UserDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.*;
import pl.gkawalec.pgk.test.annotation.PGKTestProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@PGKTestProfiles
public class TestUserUtil {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    public void createUserWithAuthorities(String email, String password, Authority authority) {
        createUserWithAuthorities(email, password, Set.of(authority));
    }

    public void createUserWithAuthorities(String email, String password, Set<Authority> authorities) {
        if (userCredentialsRepository.existsByEmail(email)) {
            throw new UnsupportedOperationException("Cannot create user because email already exists, email: " + email);
        }
        List<AuthorityEntity> authorityEntities = authorityRepository.findAllByAuthorityIn(authorities);
        RoleEntity roleEntity = insertRole(email, authorityEntities);
        UserEntity userEntity = insertUser(email);
        insertCredentials(roleEntity, userEntity, password);
    }

    private RoleEntity insertRole(String roleName, List<AuthorityEntity> authorityEntities) {
        RoleEntity roleEntity = roleRepository.findByName(roleName);
        if (Objects.nonNull(roleEntity)) {
            throw new UnsupportedOperationException("Cannot create role because roleName already exists, roleName: " + roleName);
        }
        roleEntity = RoleEntityMapper.create(roleName, null, authorityEntities);
        return roleRepository.saveAndFlush(roleEntity);
    }

    private UserEntity insertUser(String email) {
        UserDTO dto = UserDTO.builder()
                .email(email)
                .firstName(email)
                .lastName(email)
                .build();
        UserEntity userEntity = UserEntityMapper.create(dto);
        return userRepository.saveAndFlush(userEntity);
    }

    private void insertCredentials(RoleEntity roleEntity, UserEntity userEntity, String password) {
        UserCredentialsEntity credentialsEntity = UserCredentialsEntityMapper.create(userEntity, roleEntity, password);
        userCredentialsRepository.saveAndFlush(credentialsEntity);
    }

}
