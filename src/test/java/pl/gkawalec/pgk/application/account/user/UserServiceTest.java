package pl.gkawalec.pgk.application.account.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.account.user.UserUpsertDTO;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private TestUserUtil testUserUtil;

    @Test
    @DisplayName("Get a user that does not exist")
    void findUpsertById_notExists() {
        //given
        Integer userId = Integer.MIN_VALUE;

        //when
        ResponseException ex = assertThrows(ResponseException.class, () -> userService.findUpsertById(userId));

        //then
        assertEquals(ex.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(ex.getType(), ResponseExceptionType.USER_NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("User check does exist")
    void findUpsertById_exists() {
        //given
        Authority authority = Authority.ROLE_READ;
        UserEntity userEntity = testUserUtil.createUserWithAuthority("email@email", "pass", authority);

        //when
        UserUpsertDTO userFound = userService.findUpsertById(userEntity.getId());

        //then
        assertNotNull(userFound);
        assertNotNull(userFound.getUser());
        assertNotNull(userFound.getRole());
        assertEquals(userEntity.getId(), userFound.getUser().getId());
        assertEquals(userEntity.getEmail(), userFound.getUser().getEmail());
        assertEquals(userEntity.getFirstName(), userFound.getUser().getFirstName());
        assertEquals(userEntity.getLastName(), userFound.getUser().getLastName());
        assertEquals(userEntity.getDescription(), userFound.getUser().getDescription());
        assertEquals(userEntity.getDescription(), userFound.getUser().getDescription());
        assertEquals(userEntity.getPhoneNumber(), userFound.getUser().getPhoneNumber());
        assertEquals(userEntity.getVersion(), userFound.getUser().getEntityVersion());
        assertEquals(List.of(authority), userFound.getUser().getAuthorities());
    }

    @Test
    @Transactional
    @DisplayName("Correct user creation")
    void create_correct() {
        //given
        Authority authority = Authority.ROLE_READ;
        AuthorityEntity authorityEntity = authorityRepository.findByAuthority(authority);
        RoleEntity roleEntity = RoleEntityMapper.create("role", "role", authorityEntity);
        roleEntity = roleRepository.save(roleEntity);
        UserDTO userDTO = UserDTO.builder().email("a@a").firstName("name").lastName("name").description("desc").phoneNumber("+48").build();
        RoleDTO roleDTO = RoleDTO.builder().id(roleEntity.getId()).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).password("pass").build();

        //when
        UserUpsertDTO createdUser = userService.create(dto);

        //then
        assertNotNull(createdUser);
        assertNotNull(createdUser.getUser());
        assertEquals(userDTO.getEmail(), createdUser.getUser().getEmail());
        assertEquals(userDTO.getFirstName(), createdUser.getUser().getFirstName());
        assertEquals(userDTO.getLastName(), createdUser.getUser().getLastName());
        assertEquals(userDTO.getDescription(), createdUser.getUser().getDescription());
        assertEquals(userDTO.getPhoneNumber(), createdUser.getUser().getPhoneNumber());
        assertEquals(1, createdUser.getUser().getEntityVersion(), "This is the first version, so the value must be 1");
        assertEquals(List.of(authority), createdUser.getUser().getAuthorities());

        assertNotNull(createdUser.getRole());
        assertEquals(roleEntity.getId(), createdUser.getRole().getId());
        assertEquals(roleEntity.getName(), createdUser.getRole().getName());
        assertEquals(roleEntity.getDescription(), createdUser.getRole().getDescription());
        assertEquals(roleEntity.getVersion(), createdUser.getRole().getEntityVersion());
        assertEquals(Set.of(authority), createdUser.getRole().getAuthorities());
    }

    @Test
    @Transactional
    @DisplayName("The user was updated correctly")
    void update_correct() {
        //given
        Authority authority = Authority.ROLE_READ;
        AuthorityEntity authorityEntity = authorityRepository.findByAuthority(authority);
        RoleEntity roleEntity = RoleEntityMapper.create("role", "role", authorityEntity);
        roleEntity = roleRepository.save(roleEntity);
        UserDTO forCreateUserDTO = UserDTO.builder().email("a@a").firstName("name").lastName("name").description("desc").phoneNumber("+48").build();
        RoleDTO forCreateRoleDTO = RoleDTO.builder().id(roleEntity.getId()).build();
        UserUpsertDTO forCreateDTO = UserUpsertDTO.builder().user(forCreateUserDTO).role(forCreateRoleDTO).password("pass").build();
        UserUpsertDTO createdUser = userService.create(forCreateDTO);

        UserDTO editedUserDTO = UserDTO.builder()
                .id(createdUser.getUser().getId())
                .email(createdUser.getUser().getEmail())
                .firstName(createdUser.getUser().getFirstName())
                .lastName(createdUser.getUser().getLastName())
                .description("new description")
                .phoneNumber(createdUser.getUser().getPhoneNumber())
                .entityVersion(createdUser.getUser().getEntityVersion())
                .build();
        RoleDTO editedRoleDTO = RoleDTO.builder()
                .id(createdUser.getRole().getId())
                .build();
        UserUpsertDTO forUpdateDTO = UserUpsertDTO.builder().user(editedUserDTO).role(editedRoleDTO).build();

        //when
        UserUpsertDTO updatedUser = userService.update(forUpdateDTO);

        //then
        assertNotNull(updatedUser);
        assertNotNull(updatedUser.getUser());
        assertEquals(editedUserDTO.getEmail(), updatedUser.getUser().getEmail());
        assertEquals(editedUserDTO.getFirstName(), updatedUser.getUser().getFirstName());
        assertEquals(editedUserDTO.getLastName(), updatedUser.getUser().getLastName());
        assertEquals(editedUserDTO.getDescription(), updatedUser.getUser().getDescription());
        assertEquals(editedUserDTO.getPhoneNumber(), updatedUser.getUser().getPhoneNumber());
        assertEquals(createdUser.getUser().getEntityVersion() + 1, updatedUser.getUser().getEntityVersion(), "The version after the update should be incremented");
        assertEquals(List.of(authority), updatedUser.getUser().getAuthorities());

        assertNotNull(updatedUser.getRole());
        assertEquals(createdUser.getRole().getId(), updatedUser.getRole().getId());
        assertEquals(createdUser.getRole().getName(), updatedUser.getRole().getName());
        assertEquals(createdUser.getRole().getDescription(), updatedUser.getRole().getDescription());
        assertEquals(createdUser.getRole().getEntityVersion(), updatedUser.getRole().getEntityVersion());
        assertEquals(createdUser.getRole().getAuthorities(), updatedUser.getRole().getAuthorities());
    }

    @Test
    @Transactional
    @DisplayName("The user was deactivated correctly")
    void deactivate_correct() {
        //given
        Authority authority = Authority.ROLE_READ;
        AuthorityEntity authorityEntity = authorityRepository.findByAuthority(authority);
        RoleEntity roleEntity = RoleEntityMapper.create("role", "role", authorityEntity);
        roleEntity = roleRepository.save(roleEntity);
        UserDTO userDTO = UserDTO.builder().email("a@a").firstName("name").lastName("name").description("desc").phoneNumber("+48").build();
        RoleDTO roleDTO = RoleDTO.builder().id(roleEntity.getId()).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).password("pass").build();

        //when
        UserUpsertDTO createdUser = userService.create(dto);
        userService.deactivate(createdUser.getUser().getId());
    }

    @Test
    @Transactional
    @DisplayName("The user was activated correctly")
    void activate_correct() {
        //given
        Authority authority = Authority.ROLE_READ;
        AuthorityEntity authorityEntity = authorityRepository.findByAuthority(authority);
        RoleEntity roleEntity = RoleEntityMapper.create("role", "role", authorityEntity);
        roleEntity = roleRepository.save(roleEntity);
        UserDTO userDTO = UserDTO.builder().email("a@a").firstName("name").lastName("name").description("desc").phoneNumber("+48").build();
        RoleDTO roleDTO = RoleDTO.builder().id(roleEntity.getId()).build();
        UserUpsertDTO dto = UserUpsertDTO.builder().user(userDTO).role(roleDTO).password("pass").build();

        //when
        UserUpsertDTO createdUser = userService.create(dto);
        userService.activate(createdUser.getUser().getId());
    }

}
