package pl.gkawalec.pgk.api.dto.common.auditing;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuditingInfoDTO {

    AuditorDTO created;
    AuditorDTO lastModified;

}
