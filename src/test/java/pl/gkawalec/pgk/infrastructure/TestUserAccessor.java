package pl.gkawalec.pgk.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.user.UserAccessor;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.user.UserCredentialsEntity;
import pl.gkawalec.pgk.database.account.user.UserCredentialsRepository;
import pl.gkawalec.pgk.database.account.user.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestUserAccessor implements UserAccessor {

    private final UserCredentialsRepository credentialsRepository;

    private UserEntity userEntity;
    private List<Authority> authorities = new ArrayList<>();

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        fetchAuthorities();
    }

    private void fetchAuthorities() {
        if (Objects.isNull(userEntity)) {
            authorities = new ArrayList<>();
            return;
        }
        UserCredentialsEntity credentialsEntity = credentialsRepository.findByEmail(userEntity.getEmail());
        RoleEntity role = credentialsEntity.getRole();
        List<AuthorityEntity> authorities = role.getAuthorities();
        this.authorities = authorities.stream()
                .map(AuthorityEntity::getAuthority)
                .collect(Collectors.toList());
    }

    public void clearUser() {
        userEntity = null;
        authorities = new ArrayList<>();
    }

    @Override
    public UserDTO getUser() {
        return Objects.isNull(userEntity) ?
                null : UserDTO.of(userEntity, authorities);
    }

    @Override
    public Integer getUserId() {
        return Objects.isNull(userEntity) ?
                null : userEntity.getId();
    }
}
