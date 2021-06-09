package pl.gkawalec.pgk.application.account.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.common.exception.UpdatedEntityLockException;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@PGKSpringBootTest
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private TestRoleCreator roleCreator;

    @Test
    @DisplayName("Get a role that does not exist")
    void findById_notExists() {
        //given
        Integer roleId = Integer.MIN_VALUE;

        //when
        ResponseException ex = assertThrows(ResponseException.class, () -> roleService.findById(roleId));

        //then
        assertEquals(ex.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_NOT_FOUND);
    }

    @Test
    @DisplayName("Role check does exist")
    void findById_exists() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity roleEntity = roleCreator.create(roleName, Authority.ROLE_READ);
        Set<Authority> roleAuthorities = roleEntity.getAuthorities().stream().map(AuthorityEntity::getAuthority).collect(Collectors.toSet());
        Integer id = roleEntity.getId();

        //when
        RoleDTO roleFound = roleService.findById(id);

        //then
        assertNotNull(roleFound);
        assertEquals(roleEntity.getName(), roleFound.getName());
        assertEquals(roleEntity.getDescription(), roleFound.getDescription());
        assertEquals(roleAuthorities, roleFound.getAuthorities());
        assertEquals(roleEntity.getVersion(), roleFound.getEntityVersion());
        assertEquals(1, roleFound.getEntityVersion(), "This is the first version, so the value must be 1");
    }

    @Test
    @DisplayName("Get all roles without admin authority")
    void allAvailable_correct() {
        //when
        List<RoleDTO> roles = roleService.allAvailable();
        boolean hasAdminRole = false;
        for (RoleDTO role : roles) {
            if (role.getAuthorities().stream().anyMatch(Authority.ADMIN::equals)) {
                hasAdminRole = true;
                break;
            }
        }

        //then
        assertFalse(hasAdminRole, "Role with admin authority is not available for normal use");
    }

    @Test
    @Transactional
    @DisplayName("Correct role creation")
    void create_correct() {
        //given
        String name = UUID.randomUUID().toString();
        String description = "desc desc desc";
        Set<Authority> authorities = Set.of(Authority.ROLE_WRITE);
        RoleDTO dto = RoleDTO.builder().name(name).description(description).authorities(authorities).build();

        //when
        RoleDTO createdRole = roleService.create(dto);

        //then
        assertNotNull(createdRole);
        assertEquals(name, createdRole.getName());
        assertEquals(description, createdRole.getDescription());
        assertEquals(authorities, createdRole.getAuthorities());
        assertEquals(1, createdRole.getEntityVersion(), "This is the first version, so the value must be 1");
    }

    @Test
    @DisplayName("The role was updated correctly")
    void update_correct() {
        //given
        String name = UUID.randomUUID().toString();
        RoleEntity roleEntity = roleCreator.create(name, Authority.ROLE_WRITE);
        Integer id = roleEntity.getId();
        int version = roleEntity.getVersion();

        String newName = name + name;
        String newDescription = "dd desc";
        Set<Authority> newAuthorities = Set.of(Authority.ROLE_WRITE, Authority.ROLE_READ);
        RoleDTO dto = RoleDTO.builder().id(id).name(newName).description(newDescription).authorities(newAuthorities).entityVersion(version).build();

        //when
        RoleDTO updatedRole = roleService.update(dto);

        //then
        assertNotNull(updatedRole);
        assertEquals(newName, updatedRole.getName());
        assertEquals(newDescription, updatedRole.getDescription());
        assertEquals(newAuthorities, updatedRole.getAuthorities());
        assertEquals(version + 1, updatedRole.getEntityVersion(), "The version after the update should be incremented");
    }

    @Test
    @DisplayName("Updating a role that was previously changed")
    void update_UpdatedEntityLockException() {
        //given
        RoleEntity roleEntity = roleCreator.create(UUID.randomUUID().toString(), Authority.ROLE_READ);
        RoleDTO firstDTO = RoleDTO.of(roleEntity);
        RoleDTO secondDTO = RoleDTO.of(roleEntity);

        //when
        roleService.update(firstDTO);
        UpdatedEntityLockException ex = assertThrows(UpdatedEntityLockException.class, () -> roleService.update(secondDTO));

        //then
        assertNotNull(ex, "Exception, because the second requests has a different version");
    }

    @Test
    @DisplayName("The role was deleted correctly")
    void delete_correct() {
        //given
        String name = UUID.randomUUID().toString();
        RoleEntity roleEntity = roleCreator.create(name, Authority.ROLE_WRITE);

        //when
        roleService.delete(roleEntity.getId());
    }

}
