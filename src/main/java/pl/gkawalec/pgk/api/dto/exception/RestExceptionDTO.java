package pl.gkawalec.pgk.api.dto.exception;

import lombok.ToString;
import lombok.Value;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.common.exception.response.ExceptionForResponse;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

import java.util.UUID;

@Value
@ToString
public class RestExceptionDTO {

    int httpStatus;
    String errorUUID;
    ResponseExceptionType type;

    public RestExceptionDTO(ExceptionForResponse ex) {
        this.httpStatus = ex.getHttpStatus().value();
        this.errorUUID = ex.getErrorUUID();
        this.type = ex.getType();
    }

    public RestExceptionDTO() {
        this.type = ResponseExceptionType.UNEXPECTED;
        this.errorUUID = UUID.randomUUID().toString();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

}
