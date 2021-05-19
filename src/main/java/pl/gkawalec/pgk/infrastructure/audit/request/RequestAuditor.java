package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.exception.response.ExceptionForResponse;
import pl.gkawalec.pgk.common.exception.response.UnexpectedResponseException;

import java.time.Instant;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
class RequestAuditor {

    private final RequestAuditSaverAsync saver;
    private final RequestAuditUserHelper userHelper;

    Object auditRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        AuditorModel auditorModel = new AuditorModel();
        RequestAuditModel requestAuditModel = buildModel(joinPoint);
        auditorModel.setRequestAuditModel(requestAuditModel);

        try {
            auditorModel.setReturnValue(joinPoint.proceed());
        } catch (Throwable ex) {
            auditorModel.setException(wrapExceptionIfNeeded(ex));
        }

        auditorModel.finalizeBuildRequestAuditModel();
        logRequest(auditorModel.getRequestAuditModel(), auditorModel.getDurationModel());

        if (Objects.nonNull(auditorModel.getException())) {
            throw (Throwable) auditorModel.getException();
        }
        return auditorModel.getReturnValue();
    }

    private RequestAuditModel buildModel(ProceedingJoinPoint joinPoint) {
        try {
            return new RequestAuditModel(joinPoint);
        } catch (Exception ex) {
            log.error("Can't build request audit model", ex);
            return RequestAuditModel.empty();
        }
    }

    private ExceptionForResponse wrapExceptionIfNeeded(Throwable throwable) {
        if (throwable instanceof ExceptionForResponse) {
            return (ExceptionForResponse) throwable;
        }
        return new UnexpectedResponseException(throwable);
    }

    private void logRequest(RequestAuditModel requestAuditModel, AuditLogDurationModel durationModel) {
        if (!requestAuditModel.isForAudit()) {
            return;
        }
        Integer userId = userHelper.getUserId();
        AuditLogModel auditLogModel = new AuditLogModel(requestAuditModel, userId, durationModel);
        saver.save(auditLogModel);
    }

    @Getter(AccessLevel.PRIVATE)
    private static class AuditorModel {

        private final Instant startAudit;

        private Instant startProcess;
        private Instant endProcess;

        @Setter(AccessLevel.PRIVATE)
        private Object returnValue;
        @Setter(AccessLevel.PRIVATE)
        private ExceptionForResponse exception;

        private RequestAuditModel requestAuditModel;

        private AuditLogDurationModel durationModel;

        AuditorModel() {
            this.startAudit = Instant.now();
        }

        void finalizeBuildRequestAuditModel() {
            endProcess();
            requestAuditModel.setResponseObject(returnValue);
            requestAuditModel.setException(exception);
            this.durationModel = new AuditLogDurationModel(startAudit, startProcess, endProcess);
        }

        void setRequestAuditModel(RequestAuditModel requestAuditModel) {
            this.requestAuditModel = requestAuditModel;
            startProcess();
        }

        private void startProcess() {
            this.startProcess = Instant.now();
        }

        private void endProcess() {
            this.endProcess = Instant.now();
        }

    }

}
