package pl.gkawalec.pgk.api.dto.legalact;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingDTO;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class LegalActAuditingDTO implements AuditingDTO<LegalActDTO> {

    LegalActDTO dto;
    AuditingInfoDTO info;

    public static LegalActAuditingDTO of(LegalActEntity entity, AuditingInfoDTO auditing) {
        LegalActDTO legalAct = LegalActDTO.of(entity);
        return LegalActAuditingDTO.builder()
                .dto(legalAct)
                .info(auditing)
                .build();
    }

}
