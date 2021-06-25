package pl.gkawalec.pgk.database.account.user;

import pl.gkawalec.pgk.common.jpa.BaseRepository;
import pl.gkawalec.pgk.database.account.role.RoleEntity;

import java.util.List;

public interface UserCredentialsRepository extends BaseRepository<UserCredentialsEntity, String> {

    boolean existsByEmail(String email);

    UserCredentialsEntity findByEmail(String email);

    List<UserCredentialsEntity> findAllByRole(RoleEntity role);

}
