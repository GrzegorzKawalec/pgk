package pl.gkawalec.pgk.common.auditing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditorDTO;
import pl.gkawalec.pgk.common.util.DateTimeUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;
import pl.gkawalec.pgk.infrastructure.audit.database.Auditor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuditingMapper {

    private final UserRepository userRepository;

    public AuditingInfoDTO map(AuditingEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        AuditorDTO created = map(entity.getCreated());
        AuditorDTO lastModified = map(entity.getLastModified());
        return AuditingInfoDTO.builder()
                .created(created)
                .lastModified(lastModified)
                .build();
    }

    private AuditorDTO map(Auditor auditor) {
        if (Objects.isNull(auditor)) {
            return null;
        }
        Integer userId = auditor.getUserId();
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (Objects.isNull(userEntity)) {
            return AuditorDTO.builder()
                    .id(userId)
                    .date(DateTimeUtil.dateToString(auditor.getDate()))
                    .time(DateTimeUtil.timeToString(auditor.getDate()))
                    .build();
        }
        return AuditorDTO.builder()
                .id(userId)
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .date(DateTimeUtil.dateToString(auditor.getDate()))
                .time(DateTimeUtil.timeToString(auditor.getDate()))
                .build();
    }

}
