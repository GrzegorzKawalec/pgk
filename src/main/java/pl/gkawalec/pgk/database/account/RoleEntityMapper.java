package pl.gkawalec.pgk.database.account;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.api.dto.account.RoleDTO;

import java.util.List;

@UtilityClass
public class RoleEntityMapper {

    public RoleEntity create(String name, String description, AuthorityEntity authorityEntity) {
        return create(name, description, List.of(authorityEntity));
    }

    public RoleEntity create(String name, String description, List<AuthorityEntity> authorityEntities) {
        RoleEntity entity = new RoleEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setAuthorities(authorityEntities);
        return entity;
    }

    public void update(RoleDTO dto, RoleEntity entity, List<AuthorityEntity> authorityEntities) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAuthorities(authorityEntities);
        entity.setVersion(dto.getEntityVersion());
    }

}
