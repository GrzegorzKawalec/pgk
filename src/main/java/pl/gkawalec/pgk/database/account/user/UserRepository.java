package pl.gkawalec.pgk.database.account.user;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

public interface UserRepository extends BaseNumberIDRepository<UserEntity> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    UserEntity findByEmail(String email);

}
