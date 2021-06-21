package pl.gkawalec.pgk.common.exception.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.common.exception.response.ExceptionForResponse;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

import java.util.UUID;

public class AccessDeniedResponseException extends RuntimeException implements ExceptionForResponse {

    @Getter
    private final String errorUUID;
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final ResponseExceptionType type;
    @Getter
    private final Throwable causeException;

    public AccessDeniedResponseException(String message) {
        super(message);
        this.causeException = null;
        this.errorUUID = UUID.randomUUID().toString();
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.type = ResponseExceptionType.ACCESS_DENIED;
    }

}
