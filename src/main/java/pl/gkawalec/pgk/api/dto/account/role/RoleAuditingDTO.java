package pl.gkawalec.pgk.api.dto.account.role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingDTO;
import pl.gkawalec.pgk.database.account.role.RoleEntity;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class RoleAuditingDTO implements AuditingDTO<RoleDTO> {

    RoleDTO dto;
    AuditingInfoDTO info;

    public static RoleAuditingDTO of(RoleEntity entity, AuditingInfoDTO auditing) {
        RoleDTO role = RoleDTO.of(entity);
        return RoleAuditingDTO.builder()
                .dto(role)
                .info(auditing)
                .build();
    }

}
