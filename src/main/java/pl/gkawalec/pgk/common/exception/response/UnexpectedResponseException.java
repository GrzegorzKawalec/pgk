package pl.gkawalec.pgk.common.exception.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

import java.util.UUID;

public class UnexpectedResponseException extends RuntimeException implements ExceptionForResponse {

    @Getter
    private final String errorUUID;
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final ResponseExceptionType type;

    public UnexpectedResponseException(Throwable throwable) {
        super(throwable);
        this.errorUUID = UUID.randomUUID().toString();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.type = null;
    }

}
