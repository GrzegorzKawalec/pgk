package pl.gkawalec.pgk.api.dto.legalact;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.search.BaseCriteria;

@Value
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LegalActCriteria extends BaseCriteria {

    Boolean isActive = true;
    String dateOfLessThanOrEqual = null;
    String dateOfGreaterThanOrEqual = null;

}
