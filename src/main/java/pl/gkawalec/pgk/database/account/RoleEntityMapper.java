package pl.gkawalec.pgk.database.account;

import lombok.experimental.UtilityClass;

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

}
