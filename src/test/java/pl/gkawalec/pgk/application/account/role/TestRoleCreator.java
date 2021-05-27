package pl.gkawalec.pgk.application.account.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.*;
import pl.gkawalec.pgk.test.annotation.PGKTestProfiles;

@Component
@PGKTestProfiles
class TestRoleCreator {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    RoleEntity createAdmin(String roleName) {
        return create(roleName, Authority.ADMIN);
    }

    RoleEntity create(String roleName, Authority authority) {
        AuthorityEntity authorityEntity = authorityRepository.findByAuthority(authority);
        RoleEntity roleEntity = RoleEntityMapper.create(roleName, null, authorityEntity);
        roleEntity = roleRepository.save(roleEntity);
        return roleEntity;
    }

}
