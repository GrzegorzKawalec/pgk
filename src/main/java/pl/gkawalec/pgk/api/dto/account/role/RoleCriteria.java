package pl.gkawalec.pgk.api.dto.account.role;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.search.BaseCriteria;
import pl.gkawalec.pgk.common.type.Authority;

import java.util.HashSet;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class RoleCriteria extends BaseCriteria {

    Set<Authority> authorities = new HashSet<>();

}
