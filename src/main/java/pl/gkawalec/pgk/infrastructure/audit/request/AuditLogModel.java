package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.Getter;
import pl.gkawalec.pgk.common.exception.response.ExceptionForResponse;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class AuditLogModel {

    private final LocalDateTime createdDate;

    private final Integer userId;

    private final String uri;
    private final String method;

    private final String requestParams;
    private final String requestBody;
    private final String response;

    private final String errorUUID;
    private final String errorMessage;
    private final ResponseExceptionType exceptionType;

    private final AuditLogDurationModel durationModel;

    AuditLogModel(RequestAuditModel requestAuditModel, Integer userId, AuditLogDurationModel durationModel) {
        this.userId = userId;

        HttpServletRequest request = requestAuditModel.getRequest();
        this.uri = request.getRequestURI();
        this.method = request.getMethod();

        this.requestParams = requestAuditModel.getRequestParam();
        this.requestBody = requestAuditModel.getRequestBody();
        this.response = JSONConverter.toJSON(requestAuditModel.getResponseObject());

        ExceptionForResponse exception = requestAuditModel.getException();
        if (Objects.nonNull(exception)) {
            this.errorUUID = exception.getErrorUUID();
            this.errorMessage = exception.getMessage();
            this.exceptionType = exception.getType();
        } else {
            this.errorUUID = null;
            this.errorMessage = null;
            this.exceptionType = null;
        }

        this.durationModel = durationModel;
        this.createdDate = LocalDateTime.now();
    }

}
