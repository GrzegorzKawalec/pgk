package pl.gkawalec.pgk.application.account.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleRepository;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
class RoleValidator {

    private final RoleRepository roleRepository;
    private final RoleUniqueNameChecker roleUniqueNameChecker;

    void validateCreate(RoleDTO dto) {
        validateFieldDTO(dto);
        if (roleUniqueNameChecker.existsTrimName(dto.getName())) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_NAME_EXISTS);
        }
    }

    void validateUpdate(RoleDTO dto) {
        if (Objects.isNull(dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_BLANK_ID);
        }
        RoleEntity roleEntity = roleRepository.findById(dto.getId())
                .orElseThrow(() -> new ValidateResponseException(ResponseExceptionType.ROLE_NOT_FOUND));
        if (hasAdminAuthority(roleEntity)) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_CANNOT_UPDATE_ADMIN);
        }
        if (hasEmployeeAuthority(roleEntity)) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_CANNOT_UPDATE_EMPLOYEE);
        }
        validateFieldDTO(dto);
        if (roleUniqueNameChecker.existsTrimName(dto.getName(), dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_NAME_EXISTS);
        }
    }

    void validateDelete(Integer roleId) {
        if (Objects.isNull(roleId)) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_BLANK_ID);
        }
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new ValidateResponseException(ResponseExceptionType.ROLE_NOT_FOUND));
        if (hasAdminAuthority(roleEntity)) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_CANNOT_DELETE_ADMIN);
        }
        if (hasEmployeeAuthority(roleEntity)) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_CANNOT_DELETE_EMPLOYEE);
        }
    }

    private boolean hasAdminAuthority(RoleEntity roleEntity) {
        return hasAuthority(roleEntity, Authority.ADMIN);
    }

    private boolean hasEmployeeAuthority(RoleEntity roleEntity) {
        return hasAuthority(roleEntity, Authority.EMPLOYEE);
    }

    private boolean hasAuthority(RoleEntity roleEntity, Authority authority) {
        List<AuthorityEntity> authorities = roleEntity.getAuthorities();
        if (CollectionUtil.isEmpty(authorities)) {
            return false;
        }
        return authorities.stream()
                .anyMatch(authorityEntity -> authority.equals(authorityEntity.getAuthority()));
    }

    private void validateFieldDTO(RoleDTO dto) {
        if (StringUtil.isBlank(dto.getName())) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_BLANK_NAME);
        }
        if (CollectionUtil.isEmpty(dto.getAuthorities())) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_EMPTY_AUTHORITIES);
        }
        if (dto.getAuthorities().contains(Authority.ADMIN)) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_SET_ADMIN_AUTHORITY);
        }
        if (dto.getAuthorities().contains(Authority.EMPLOYEE)) {
            throw new ValidateResponseException(ResponseExceptionType.ROLE_SET_EMPLOYEE_AUTHORITY);
        }
    }

}
