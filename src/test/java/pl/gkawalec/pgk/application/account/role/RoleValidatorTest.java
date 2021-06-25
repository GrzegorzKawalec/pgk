package pl.gkawalec.pgk.application.account.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class RoleValidatorTest {

    @Autowired
    private RoleValidator validator;

    @Autowired
    private TestRoleCreator roleCreator;

    @Test
    @DisplayName("Validate DTO for new role with blank name")
    void validateCreate_blankName() {
        //given
        RoleDTO dto = RoleDTO.builder().build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST, "Error resulting from data validation, therefore the status should be " + HttpStatus.BAD_REQUEST);
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_BLANK_NAME);
    }

    @Test
    @DisplayName("Validate DTO for new role without authorities")
    void validateCreate_emptyAuthorities() {
        //given
        RoleDTO dto = RoleDTO.builder().name("name").authorities(Collections.emptySet()).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_EMPTY_AUTHORITIES);
    }

    @Test
    @DisplayName("Validate DTO for new role with ADMIN authorities")
    void validateCreate_addAdminAuthority() {
        //given
        RoleDTO dto = RoleDTO.builder().name("name").authorities(Set.of(Authority.ADMIN)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_SET_ADMIN_AUTHORITY);
    }

    @Test
    @DisplayName("Validate DTO for new role with GUEST authorities")
    void validateCreate_addGuestAuthority() {
        //given
        RoleDTO dto = RoleDTO.builder().name("name").authorities(Set.of(Authority.GUEST)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_SET_GUEST_AUTHORITY);
    }

    @Test
    @DisplayName("Validate DTO for new role with exists name")
    void validateCreate_existsName() {
        //given
        String roleName = UUID.randomUUID().toString();
        roleCreator.createAdmin(roleName);
        RoleDTO dto = RoleDTO.builder().name(roleName).authorities(Set.of(Authority.ROLE_WRITE)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_NAME_EXISTS);
    }

    @Test
    @DisplayName("Validate correct DTO for new role")
    void validateCreate_correct() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleDTO dto = RoleDTO.builder().name(roleName).authorities(Set.of(Authority.ROLE_WRITE)).build();

        //when
        assertDoesNotThrow(() -> validator.validateCreate(dto));
    }

    @Test
    @DisplayName("Validate DTO for an existing role without role id")
    void validateUpdate_withoutId() {
        //given
        RoleDTO dto = RoleDTO.builder().build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_BLANK_ID);
    }

    @Test
    @DisplayName("Validate DTO for an existing role with role id but not found in database")
    void validateUpdate_notFoundId() {
        //given
        RoleDTO dto = RoleDTO.builder().id(Integer.MIN_VALUE).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_NOT_FOUND);
    }

    @Test
    @DisplayName("Validate DTO for an existing role with ADMIN role")
    void validateUpdate_adminRole() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity admin = roleCreator.createAdmin(roleName);
        RoleDTO dto = RoleDTO.builder().id(admin.getId()).name(roleName).authorities(Set.of(Authority.ADMIN)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_CANNOT_UPDATE_ADMIN);
    }

    @Test
    @DisplayName("Validate DTO for an existing role with GUEST role")
    void validateUpdate_guestRole() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity guest = roleCreator.create(roleName, Authority.GUEST);
        RoleDTO dto = RoleDTO.builder().id(guest.getId()).name(roleName).authorities(Set.of(Authority.GUEST)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_CANNOT_UPDATE_GUEST);
    }

    @Test
    @DisplayName("Validate DTO for an existing role without name")
    void validateUpdate_withoutName() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity role = roleCreator.create(roleName, Authority.ROLE_WRITE);
        RoleDTO dto = RoleDTO.builder().id(role.getId()).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_BLANK_NAME);
    }

    @Test
    @DisplayName("Validate DTO for an existing role without authorities")
    void validateUpdate_emptyAuthorities() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity role = roleCreator.create(roleName, Authority.ROLE_WRITE);
        RoleDTO dto = RoleDTO.builder().id(role.getId()).name(roleName).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_EMPTY_AUTHORITIES);
    }

    @Test
    @DisplayName("Validate DTO for an existing role with ADMIN authorities")
    void validateUpdate_addAdminAuthority() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity role = roleCreator.create(roleName, Authority.ROLE_WRITE);
        RoleDTO dto = RoleDTO.builder().id(role.getId()).name(roleName).authorities(Set.of(Authority.ADMIN)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_SET_ADMIN_AUTHORITY);
    }

    @Test
    @DisplayName("Validate DTO for an existing role with GUEST authorities")
    void validateUpdate_addAGuestAuthority() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity role = roleCreator.create(roleName, Authority.ROLE_WRITE);
        RoleDTO dto = RoleDTO.builder().id(role.getId()).name(roleName).authorities(Set.of(Authority.GUEST)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_SET_GUEST_AUTHORITY);
    }

    @Test
    @DisplayName("Validate DTO for an existing role with exists name")
    void validateUpdate_existsName() {
        //given
        String roleNameExist = UUID.randomUUID().toString();
        roleCreator.create(roleNameExist, Authority.ROLE_WRITE);
        String roleNameNew = UUID.randomUUID().toString();
        RoleEntity roleNew = roleCreator.create(roleNameNew, Authority.ROLE_WRITE);
        RoleDTO dto = RoleDTO.builder().id(roleNew.getId()).name(roleNameExist).authorities(Set.of(Authority.ROLE_WRITE)).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_NAME_EXISTS);
    }

    @Test
    @DisplayName("Validate correct DTO for an existing role")
    void validateUpdate_correct() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity role = roleCreator.create(roleName, Authority.ROLE_WRITE);
        RoleDTO dto = RoleDTO.builder().id(role.getId()).name(roleName).authorities(Set.of(Authority.ROLE_WRITE)).build();

        //when
        assertDoesNotThrow(() -> validator.validateUpdate(dto));
    }

    @Test
    @DisplayName("Validation of role deletion: id not given")
    void validateDelete_withoutId() {
        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateDelete(null));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_BLANK_ID);
    }

    @Test
    @DisplayName("Validation of role deletion: role not found")
    void validateDelete_notFoundId() {
        //given
        Integer roleId = Integer.MIN_VALUE;

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateDelete(roleId));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_NOT_FOUND);
    }

    @Test
    @DisplayName("Validation of role deletion: role with ADMIN authorities")
    void validateDelete_deleteAdminAuthority() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity role = roleCreator.create(roleName, Authority.ADMIN);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateDelete(role.getId()));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_CANNOT_DELETE_ADMIN);
    }

    @Test
    @DisplayName("Validation of role deletion: role with GUEST authorities")
    void validateDelete_deleteGuestAuthority() {
        //given
        String roleName = UUID.randomUUID().toString();
        RoleEntity role = roleCreator.create(roleName, Authority.GUEST);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateDelete(role.getId()));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_CANNOT_DELETE_GUEST);
    }

}
