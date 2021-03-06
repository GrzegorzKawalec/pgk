package pl.gkawalec.pgk.common.exception.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

import java.util.UUID;

public class ResponseException extends RuntimeException implements ExceptionForResponse {

    @Getter
    private final String errorUUID;
    @Getter
    protected HttpStatus httpStatus;
    @Getter
    private final ResponseExceptionType type;
    @Getter
    private final Throwable causeException;

    public ResponseException(ResponseExceptionType type) {
        this(type, null);
    }

    public ResponseException(ResponseExceptionType type, String message) {
        this(null, type, message);
    }

    public ResponseException(Throwable throwable, ResponseExceptionType type) {
        this(throwable, type, null);
    }

    public ResponseException(Throwable throwable, ResponseExceptionType type, String message) {
        super(message, throwable);
        this.type = type;
        this.causeException = throwable;
        this.errorUUID = UUID.randomUUID().toString();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ResponseException httpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

}
