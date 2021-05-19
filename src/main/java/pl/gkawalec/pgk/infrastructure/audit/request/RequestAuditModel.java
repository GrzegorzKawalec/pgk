package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import pl.gkawalec.pgk.common.exception.response.ExceptionForResponse;

import javax.servlet.http.HttpServletRequest;

@Getter(AccessLevel.PACKAGE)
class RequestAuditModel {

    private final boolean forAudit;
    private final String requestBody;
    private final String requestParam;
    private final HttpServletRequest request;

    @Setter(AccessLevel.PACKAGE)
    private Object responseObject;

    @Setter(AccessLevel.PACKAGE)
    private ExceptionForResponse exception;

    RequestAuditModel(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = RequestAuditUtil.getHttpServletRequest();
        this.forAudit = RequestAuditUtil.canAuditRequestMethod(request, joinPoint);
        if (isForAudit()) {
            this.request = request;
            this.requestParam = RequestAuditUtil.getRequestParam(request);
            this.requestBody = RequestAuditUtil.getRequestBody(joinPoint);
        } else {
            this.request = null;
            this.requestParam = null;
            this.requestBody = null;
        }
    }

    static RequestAuditModel empty() {
        return new RequestAuditModel(null);
    }

}
