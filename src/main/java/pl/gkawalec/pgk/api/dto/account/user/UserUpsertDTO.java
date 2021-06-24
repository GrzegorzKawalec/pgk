package pl.gkawalec.pgk.api.dto.account.user;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.common.annotation.request.NotAuditedRequestType;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.user.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class UserUpsertDTO {

    RoleDTO role;
    UserDTO user;

    @NotAuditedRequestType
    String password;

    public static UserUpsertDTO of(UserEntity userEntity, RoleEntity roleEntity) {
        List<Authority> authorities = roleEntity.getAuthorities().stream()
                .map(AuthorityEntity::getAuthority)
                .collect(Collectors.toList());
        UserDTO user = UserDTO.of(userEntity, authorities);
        RoleDTO role = RoleDTO.of(roleEntity);
        return UserUpsertDTO.builder()
                .user(user)
                .role(role)
                .build();
    }

}
