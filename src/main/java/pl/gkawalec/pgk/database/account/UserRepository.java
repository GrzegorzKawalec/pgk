package pl.gkawalec.pgk.database.account;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

import java.util.Optional;

public interface UserRepository extends BaseNumberIDRepository<UserEntity> {

    boolean existsByEmail(String email);
    UserEntity findByEmail(String email);

}