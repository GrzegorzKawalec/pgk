package pl.gkawalec.pgk.api.dto.account.user;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.search.BaseCriteria;

import java.util.HashSet;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserCriteria extends BaseCriteria {

    Set<Integer> roleIds = new HashSet<>();

}
