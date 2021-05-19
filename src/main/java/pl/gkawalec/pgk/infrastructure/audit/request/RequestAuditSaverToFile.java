package pl.gkawalec.pgk.infrastructure.audit.request;

import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.Loggers;

import java.util.Objects;

@Component
class RequestAuditSaverToFile {

    void save(AuditLogModel auditLogModel, Long auditEntityId) {
        StringBuilder logInfo = new StringBuilder();
        logInfo.append("[uri: " ).append(auditLogModel.getUri()).append("] ");
        logInfo.append("[method: ").append(auditLogModel.getMethod()).append("] ");

        if (Objects.nonNull(auditLogModel.getUserId())) {
            logInfo.append("[userId: ").append(auditLogModel.getUserId()).append("] ");
        }

        if (Objects.nonNull(auditEntityId)) {
            logInfo.append("[auditEntityId: ").append(auditEntityId).append("] ");
        } else {
            appendAdditionalInfo(auditLogModel, logInfo);
        }

        boolean hasError = false;
        if (Objects.nonNull(auditLogModel.getErrorUUID())) {
            hasError = true;
            appendErrorInfo(auditLogModel, logInfo);
        }

        if (hasError) {
            Loggers.AUDIT_REQUEST.error(logInfo.toString());
        } else {
            Loggers.AUDIT_REQUEST.info(logInfo.toString());
        }
    }

    private void appendAdditionalInfo(AuditLogModel auditLogModel, StringBuilder logInfo) {
        if (Objects.nonNull(auditLogModel.getRequestParams())) {
            logInfo.append("[params: ").append(auditLogModel.getRequestParams()).append("] ");
        }
        if (Objects.nonNull(auditLogModel.getRequestBody())) {
            logInfo.append("[body: ").append(auditLogModel.getRequestBody()).append("] ");
        }
        if (Objects.nonNull(auditLogModel.getResponse())) {
            logInfo.append("[response: ").append(auditLogModel.getResponse()).append("] ");
        }
        logInfo.append("[durationProcess: ").append(auditLogModel.getDurationModel().getDurationProcessMillis()).append("] ");
        logInfo.append("[durationAudit: ").append(auditLogModel.getDurationModel().getDurationAuditMillis()).append("] ");
    }

    private void appendErrorInfo(AuditLogModel auditLogModel, StringBuilder logInfo) {
        if (Objects.nonNull(auditLogModel.getErrorUUID())) {
            logInfo.append("[errorUUID: ").append(auditLogModel.getErrorUUID()).append("] ");
        }
        if (Objects.nonNull(auditLogModel.getExceptionType())) {
            logInfo.append("[errorType: ").append(auditLogModel.getExceptionType()).append("] ");
        }
        if (Objects.nonNull(auditLogModel.getErrorMessage())) {
            logInfo.append("[errorMessage: ").append(auditLogModel.getErrorMessage()).append("] ");
        }
    }

}
