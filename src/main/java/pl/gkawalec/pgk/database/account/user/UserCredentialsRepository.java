package pl.gkawalec.pgk.database.account.user;

import pl.gkawalec.pgk.common.jpa.BaseRepository;

public interface UserCredentialsRepository extends BaseRepository<UserCredentialsEntity, String> {

    boolean existsByEmail(String email);

    UserCredentialsEntity findByEmail(String email);

}
