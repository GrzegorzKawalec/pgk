package pl.gkawalec.pgk.api.dto.project;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Sort;
import pl.gkawalec.pgk.api.dto.common.search.BaseCriteria;

@Value
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProjectCriteria extends BaseCriteria {

    Boolean isActive = true;

    Long legalActId = null;
    Long participantId = null;

    Sort.Direction orderDirection = Sort.Direction.ASC;
    ProjectOrderByType orderBy = ProjectOrderByType.name;

}
