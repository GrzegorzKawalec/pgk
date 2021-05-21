package pl.gkawalec.pgk.database.account;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;
import pl.gkawalec.pgk.common.type.Authority;

import java.util.Optional;

public interface AuthorityRepository extends BaseNumberIDRepository<AuthorityEntity> {

    Optional<AuthorityEntity> findFirstByOrderByIdDesc();
    AuthorityEntity findByAuthority(Authority authority);

}
