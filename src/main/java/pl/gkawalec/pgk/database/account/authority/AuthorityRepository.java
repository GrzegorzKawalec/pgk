package pl.gkawalec.pgk.database.account.authority;

import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;
import pl.gkawalec.pgk.common.type.Authority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends BaseNumberIDRepository<AuthorityEntity> {

    boolean existsByAuthority(Authority authority);
    AuthorityEntity findByAuthority(Authority authority);
    Optional<AuthorityEntity> findFirstByOrderByIdDesc();
    List<AuthorityEntity> findAllByAuthorityIn(Collection<Authority> authorities);

}
