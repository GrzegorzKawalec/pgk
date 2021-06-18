package pl.gkawalec.pgk.api.dto.account.user;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingDTO;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;

@Value
@Builder
public class UserAuditingDTO implements AuditingDTO<UserUpsertDTO> {

    UserUpsertDTO dto;
    AuditingInfoDTO info;

}
