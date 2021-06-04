package pl.gkawalec.pgk.api.dto.account;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.database.account.AuthorityEntity;
import pl.gkawalec.pgk.database.account.RoleEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder
public class RoleDTO {

    Integer id;
    String name;
    String description;
    @Builder.Default
    Set<Authority> authorities = Collections.emptySet();

    Integer entityVersion;

    public static RoleDTO of(RoleEntity entity) {
        Set<Authority> authorities = CollectionUtil.isEmpty(entity.getAuthorities()) ?
                Collections.emptySet() :
                entity.getAuthorities().stream().map(AuthorityEntity::getAuthority).collect(Collectors.toSet());

        return RoleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .authorities(authorities)
                .entityVersion(entity.getVersion())
                .build();
    }

}
