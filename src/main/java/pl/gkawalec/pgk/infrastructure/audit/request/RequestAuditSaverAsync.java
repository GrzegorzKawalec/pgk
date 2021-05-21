package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RequestAuditSaverAsync {

    private final RequestAuditSaverToDatabase saverToDatabase;
    private final RequestAuditSaverToFile saverToFile;

    @Async("auditRequest")
    public void save(AuditLogModel auditLogModel) {
        auditLogModel.getDurationModel().calculateDuration();
        Long auditEntityId = saveToDatabase(auditLogModel);
        saveToFile(auditLogModel, auditEntityId);
    }

    private Long saveToDatabase(AuditLogModel auditLogModel) {
        Long auditEntityId = null;
        try {
            auditEntityId = saverToDatabase.save(auditLogModel);
        } catch (Exception ex) {
            String message = "Audit request information could not be saved to the database";
            logErrorMessage(auditLogModel, message, ex);
        }
        return auditEntityId;
    }

    private void saveToFile(AuditLogModel auditLogModel, Long auditEntityId) {
        try {
            saverToFile.save(auditLogModel, auditEntityId);
        } catch (Exception ex) {
            String message = "Audit request information could not be saved to a file";
            logErrorMessage(auditLogModel, message, ex);
        }
    }

    private void logErrorMessage(AuditLogModel auditLogModel, String message, Exception ex) {
        log.error(message + ", auditLogModel: {}", auditLogModel, ex);
    }

}
