package pl.gkawalec.pgk.database.account;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

public interface RoleRepository extends BaseNumberIDRepository<RoleEntity> {

    RoleEntity findByName(String name);

}
