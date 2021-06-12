package pl.gkawalec.pgk.api.dto.account.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.user.UserSearchEntity;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class UserSearchDTO {

    UserDTO user;
    RoleDTO role;

    public static UserSearchDTO of(UserSearchEntity entity) {
        RoleEntity roleEntity = entity.getCredentials().getRole();
        UserDTO user = UserDTO.of(entity);
        RoleDTO role = RoleDTO.of(roleEntity);
        return UserSearchDTO.builder()
                .user(user)
                .role(role)
                .build();
    }

}
