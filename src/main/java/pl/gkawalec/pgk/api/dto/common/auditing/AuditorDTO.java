package pl.gkawalec.pgk.api.dto.common.auditing;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuditorDTO {

    String firstName;
    String lastName;
    String email;
    Integer id;

    String date;
    String time;

}
