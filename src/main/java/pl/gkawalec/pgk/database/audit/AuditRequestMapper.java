package pl.gkawalec.pgk.database.audit;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.infrastructure.audit.request.AuditLogModel;

@UtilityClass
public class AuditRequestMapper {

    public AuditRequestEntity create(AuditLogModel model) {
        AuditRequestEntity entity = new AuditRequestEntity();
        entity.setUserId(model.getUserId());
        entity.setReqUri(model.getUri());
        entity.setReqMethod(model.getMethod());
        entity.setReqParam(model.getRequestParams());
        entity.setReqBody(model.getRequestBody());
        entity.setResponse(model.getResponse());
        entity.setErrorUuid(model.getErrorUUID());
        entity.setErrorType(model.getExceptionType());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setProcessDurationMillis(model.getDurationModel().getDurationProcessMillis());
        entity.setAuditDurationMillis(model.getDurationModel().getDurationAuditMillis());
        return entity;
    }

}
