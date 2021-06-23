package pl.gkawalec.pgk.application.account.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.account.user.UserUpsertDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.database.account.user.UserCredentialsEntity;
import pl.gkawalec.pgk.database.account.user.UserCredentialsRepository;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
class UserValidator {

    private final UserUniqueEmailChecker uniqueEmailChecker;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    void validateDeactivate(Integer userId) {
        UserEntity userEntity = findUser(userId);
        checkIsAdmin(userEntity);
    }

    void validateCreate(UserUpsertDTO dto) {
        validateFieldDTO(dto);
        if (Objects.isNull(dto.getPassword())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_EMPTY_PASSWORD);
        }
        if (uniqueEmailChecker.existsTrimEmail(dto.getUser().getEmail())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_EMAIL_EXISTS);
        }
        checkRoleInDatabase(dto.getRole());
    }

    void validateUpdate(UserUpsertDTO dto) {
        validateFieldDTO(dto);
        if (StringUtil.isNotBlank(dto.getPassword())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_CANNOT_CHANGE_PASSWORD);
        }
        if (Objects.isNull(dto.getUser().getId())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_BLANK_USER_ID);
        }
        UserDTO userDTO = dto.getUser();
        UserEntity userEntity = findUser(userDTO.getId());
        if (!userEntity.getEmail().equals(userDTO.getEmail())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_CANNOT_CHANGE_EMAIL);
        }
        checkIsAdmin(userEntity);
        checkRoleInDatabase(dto.getRole());
    }

    private void validateFieldDTO(UserUpsertDTO dto) {
        if (Objects.isNull(dto.getUser())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_EMPTY_USER_DATA);
        }
        if (Objects.isNull(dto.getRole())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_EMPTY_ROLE_DATA);
        }
        validateUserFieldDTO(dto.getUser());
        validateRoleFieldDTO(dto.getRole());
    }

    private void validateUserFieldDTO(UserDTO userDTO) {
        if (StringUtil.isBlank(userDTO.getEmail())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_BLANK_EMAIL);
        }
        if (StringUtil.isBlank(userDTO.getFirstName())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_BLANK_FIRST_NAME);
        }
        if (StringUtil.isBlank(userDTO.getLastName())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_BLANK_LAST_NAME);
        }
    }

    private void validateRoleFieldDTO(RoleDTO roleDTO) {
        if (Objects.isNull(roleDTO.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.USER_BLANK_ROLE_ID);
        }
    }

    private void checkIsAdmin(UserEntity userEntity) {
        UserCredentialsEntity credentialsEntity = userCredentialsRepository.findByEmail(userEntity.getEmail());
        RoleEntity roleEntity = credentialsEntity.getRole();
        if (hasAdminAuthority(roleEntity)) {
            throw new ValidateResponseException(ResponseExceptionType.USER_CANNOT_UPDATE_ADMIN_ROLE);
        }
    }

    private void checkRoleInDatabase(RoleDTO roleDTO) {
        RoleEntity roleEntity = roleRepository.findById(roleDTO.getId())
                .orElseThrow(() -> new ValidateResponseException(ResponseExceptionType.ROLE_NOT_FOUND));
        if (hasAdminAuthority(roleEntity)) {
            throw new ValidateResponseException(ResponseExceptionType.USER_CANNOT_SET_ADMIN_ROLE);
        }
    }

    private boolean hasAdminAuthority(RoleEntity roleEntity) {
        if (Objects.isNull(roleEntity) || CollectionUtil.isEmpty(roleEntity.getAuthorities())) {
            return false;
        }
        return roleEntity.getAuthorities().stream()
                .map(AuthorityEntity::getAuthority)
                .anyMatch(Authority.ADMIN::equals);
    }

    private UserEntity findUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ValidateResponseException(ResponseExceptionType.USER_NOT_FOUND));
    }

}
