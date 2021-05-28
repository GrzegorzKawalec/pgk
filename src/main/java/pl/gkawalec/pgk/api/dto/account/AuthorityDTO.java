package pl.gkawalec.pgk.api.dto.account;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.common.type.Authority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Value
@Builder
public class AuthorityDTO {

    @Builder.Default
    Set<Authority> authorities = Collections.emptySet();

}
