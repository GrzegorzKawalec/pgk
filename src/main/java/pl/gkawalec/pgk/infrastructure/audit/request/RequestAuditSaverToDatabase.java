package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.database.audit.AuditRequestEntity;
import pl.gkawalec.pgk.database.audit.AuditRequestMapper;
import pl.gkawalec.pgk.database.audit.AuditRequestRepository;

@Component
@RequiredArgsConstructor
class RequestAuditSaverToDatabase {

    private final AuditRequestRepository repository;

    Long save(AuditLogModel auditLogModel) {
        AuditRequestEntity entity = AuditRequestMapper.create(auditLogModel);
        return repository.save(entity).getId();
    }

}
