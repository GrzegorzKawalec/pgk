package pl.gkawalec.pgk.api.dto.common.auditing;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuditingDTO {

    AuditorDTO created;
    AuditorDTO lastModified;

}
