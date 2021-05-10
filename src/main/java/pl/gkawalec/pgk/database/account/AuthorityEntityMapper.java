package pl.gkawalec.pgk.database.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.type.Authority;

@Component
@RequiredArgsConstructor
public class AuthorityEntityMapper {

    private final AuthorityRepository authorityRepository;

    public AuthorityEntity create(Authority authority) {
        short id = authorityRepository.findFirstByOrderByIdDesc()
                .map(AuthorityEntity::getId)
                .orElse((short) 0);
        id += 1;
        return new AuthorityEntity(id, authority);
    }

}
