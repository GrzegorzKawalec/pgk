package pl.gkawalec.pgk.database.account.user;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

public interface UserRepository extends BaseNumberIDRepository<UserEntity> {

    boolean existsByEmail(String email);
    UserEntity findByEmail(String email);

}
