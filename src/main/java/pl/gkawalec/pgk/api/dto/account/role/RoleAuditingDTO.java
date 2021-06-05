package pl.gkawalec.pgk.api.dto.account.role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingDTO;
import pl.gkawalec.pgk.database.account.role.RoleEntity;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class RoleAuditingDTO {

    RoleDTO role;
    AuditingDTO auditing;

    public static RoleAuditingDTO of(RoleEntity entity, AuditingDTO auditing) {
        RoleDTO role = RoleDTO.of(entity);
        return RoleAuditingDTO.builder()
                .role(role)
                .auditing(auditing)
                .build();
    }

}
