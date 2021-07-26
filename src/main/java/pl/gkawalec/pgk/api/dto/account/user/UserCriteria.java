package pl.gkawalec.pgk.api.dto.account.user;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.search.BaseCriteria;

import java.util.HashSet;
import java.util.Set;

@Value
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserCriteria extends BaseCriteria {

    Boolean isActive = true;
    Set<Integer> roleIds = new HashSet<>();

    Boolean orderByRole = false;
    Boolean orderByRoleAsc = true;

}
