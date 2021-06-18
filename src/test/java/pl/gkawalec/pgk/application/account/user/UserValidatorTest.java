package pl.gkawalec.pgk.application.account.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.account.user.UserUpsertDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.authority.AuthorityRepository;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntityMapper;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class UserValidatorTest {

    private final String email = "email@email";

    @Autowired
    private UserValidator validator;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private TestUserUtil testUserUtil;

    @Test
    @DisplayName("Validate DTO for new user without user data")
    void validateCreate_withoutUser() {
        //given
        UserUpsertDTO dto = UserUpsertDTO.builder().build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST, "Error resulting from data validation, therefore the status should be " + HttpStatus.BAD_REQUEST);
        assertEquals(ex.getType(), ResponseExceptionType.USER_EMPTY_USER_DATA);
    }

    @Test
    @DisplayName("Validate DTO for new user without role data")
    void validateCreate_withoutRole() {
        //given
        UserDTO userDTO = UserDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_EMPTY_ROLE_DATA);
    }

    @Test
    @DisplayName("Validate DTO for new user with blank email")
    void validateCreate_blankEmail() {
        //given
        UserDTO userDTO = UserDTO.builder().email("   ").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_EMAIL);
    }

    @Test
    @DisplayName("Validate DTO for new user with blank first name")
    void validateCreate_blankFirstName() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_FIRST_NAME);
    }

    @Test
    @DisplayName("Validate DTO for new user with blank last name")
    void validateCreate_blankLastName() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_LAST_NAME);
    }

    @Test
    @DisplayName("Validate DTO for new user without role id")
    void validateCreate_withoutRoleId() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_ROLE_ID);
    }

    @Test
    @DisplayName("Validate DTO for new user without password")
    void validateCreate_withoutPassword() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().id(1).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_EMPTY_PASSWORD);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new user with exists email")
    void validateCreate_existEmail() {
        //given
        UserDTO userDTO = UserDTO.builder().email(email).firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().id(1).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).password("pass").build();
        testUserUtil.insertUser(email);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_EMAIL_EXISTS);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new user with exists email")
    void validateCreate_notFoundRoleById() {
        //given
        UserDTO userDTO = UserDTO.builder().email("a@a").firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().id(Integer.MIN_VALUE).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).password("pass").build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new user with admin role")
    void validateCreate_withAdminRole() {
        //given
        AuthorityEntity authorityEntity = authorityRepository.findByAuthority(Authority.ADMIN);
        RoleEntity roleEntity = RoleEntityMapper.create("role", "role", authorityEntity);
        roleEntity = roleRepository.save(roleEntity);
        UserDTO userDTO = UserDTO.builder().email("a@a").firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().id(roleEntity.getId()).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).password("pass").build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_CANNOT_SET_ADMIN_ROLE);
    }

    @Test
    @DisplayName("Validate DTO for an existing user without user dto")
    void validateUpdate_withoutUser() {
        //given
        UserUpsertDTO dto = UserUpsertDTO.builder().build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_EMPTY_USER_DATA);
    }

    @Test
    @DisplayName("Validate DTO for an existing user without user dto")
    void validateUpdate_withoutRole() {
        //given
        UserDTO userDTO = UserDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_EMPTY_ROLE_DATA);
    }

    @Test
    @DisplayName("Validate DTO for an existing user with blank email")
    void validateUpdate_blankEmail() {
        //given
        UserDTO userDTO = UserDTO.builder().email("   ").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_EMAIL);
    }

    @Test
    @DisplayName("Validate DTO for an existing user with blank first name")
    void validateUpdate_blankFirstName() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_FIRST_NAME);
    }

    @Test
    @DisplayName("Validate DTO for an existing user with blank last name")
    void validateUpdate_blankLastName() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_LAST_NAME);
    }

    @Test
    @DisplayName("Validate DTO for an existing user without role id")
    void validateUpdate_withoutRoleId() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_ROLE_ID);
    }

    @Test
    @DisplayName("Validate DTO for an existing user with password")
    void validateUpdate_withPassword() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().id(1).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).password("pass").build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_CANNOT_CHANGE_PASSWORD);
    }

    @Test
    @DisplayName("Validate DTO for an existing user without user id")
    void validateUpdate_withoutUserId() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("name").build();
        RoleDTO roleDTO = RoleDTO.builder().id(1).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_BLANK_USER_ID);
    }

    @Test
    @DisplayName("Validate DTO for an existing user with user id but not found in database")
    void validateUpdate_notFoundUserById() {
        //given
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("name").id(Integer.MIN_VALUE).build();
        RoleDTO roleDTO = RoleDTO.builder().id(1).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing user with different email")
    void validateUpdate_differentEmail() {
        //given
        UserEntity userEntity = testUserUtil.insertUser(email);
        UserDTO userDTO = UserDTO.builder().email("aa@aa").firstName("name").lastName("name").id(userEntity.getId()).build();
        RoleDTO roleDTO = RoleDTO.builder().id(1).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_CANNOT_CHANGE_EMAIL);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing user for admin authority")
    void validateUpdate_forAdminAuthority() {
        //given
        UserEntity userEntity = testUserUtil.createUserWithAuthority(email, "pass", Authority.ADMIN);
        UserDTO userDTO = UserDTO.builder().email(email).firstName("name").lastName("name").id(userEntity.getId()).build();
        RoleDTO roleDTO = RoleDTO.builder().id(1).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_CANNOT_UPDATE_ADMIN_ROLE);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing user with exists email")
    void validateUpdate_notFoundRoleById() {
        //given
        UserEntity userEntity = testUserUtil.createUserWithAuthority(email, "pass", Authority.ROLE_READ);
        UserDTO userDTO = UserDTO.builder().email(email).firstName("name").lastName("name").id(userEntity.getId()).build();
        RoleDTO roleDTO = RoleDTO.builder().id(Integer.MIN_VALUE).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.ROLE_NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing user with admin role")
    void validateUpdate_withAdminRole() {
        //given
        AuthorityEntity authorityEntity = authorityRepository.findByAuthority(Authority.ADMIN);
        RoleEntity roleEntity = RoleEntityMapper.create("role", "role", authorityEntity);
        roleEntity = roleRepository.save(roleEntity);
        UserEntity userEntity = testUserUtil.createUserWithAuthority(email, "pass", Authority.ROLE_READ);
        UserDTO userDTO = UserDTO.builder().email(email).firstName("name").lastName("name").id(userEntity.getId()).build();
        RoleDTO roleDTO = RoleDTO.builder().id(roleEntity.getId()).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).build();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.USER_CANNOT_SET_ADMIN_ROLE);
    }

}