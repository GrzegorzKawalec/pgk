package pl.gkawalec.pgk.api.dto.common;

import lombok.Value;
import org.springframework.data.domain.Sort;

@Value
public class SortDTO {

    String property;
    Sort.Direction direction;

}
