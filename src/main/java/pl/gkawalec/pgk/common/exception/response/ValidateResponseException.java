package pl.gkawalec.pgk.common.exception.response;

import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

public class ValidateResponseException extends ResponseException {

    public ValidateResponseException(ResponseExceptionType type) {
        this(type, null);
    }

    public ValidateResponseException(ResponseExceptionType type, String message) {
        super(type, message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

}
