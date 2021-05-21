package pl.gkawalec.pgk.common.exception.response;

import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

public interface ExceptionForResponse {

    String getMessage();

    String getErrorUUID();

    HttpStatus getHttpStatus();

    ResponseExceptionType getType();

    Throwable getCauseException();

}
