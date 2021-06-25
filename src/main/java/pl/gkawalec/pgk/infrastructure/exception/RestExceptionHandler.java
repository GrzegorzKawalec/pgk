package pl.gkawalec.pgk.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.gkawalec.pgk.api.dto.exception.RestExceptionDTO;
import pl.gkawalec.pgk.common.exception.UpdatedEntityLockException;
import pl.gkawalec.pgk.common.exception.response.ExceptionForResponse;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Throwable.class)
    private ResponseEntity<RestExceptionDTO> exceptionHandler(Throwable throwable) {
        return isExceptionForResponse(throwable) ?
                forResponseHandler((ExceptionForResponse) throwable) :
                defaultHandler(throwable);
    }

    private boolean isExceptionForResponse(Throwable throwable) {
        return throwable instanceof RuntimeException &&
                throwable instanceof ExceptionForResponse;
    }

    private ResponseEntity<RestExceptionDTO> defaultHandler(Throwable throwable) {
        RestExceptionDTO exceptionDTO = new RestExceptionDTO();
        String message = "RestExceptionDTO: [" + exceptionDTO + "]. Message: [" + throwable.getMessage() + "].";
        log.error(message, throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionDTO);
    }

    private ResponseEntity<RestExceptionDTO> forResponseHandler(ExceptionForResponse ex) {
        if (hasUpdatedEntityLockException(ex)) {
            return updatedEntityLockExceptionHandler((UpdatedEntityLockException) ex.getCauseException());
        }
        RestExceptionDTO exceptionDTO = new RestExceptionDTO(ex);
        logExceptionMessage(exceptionDTO, ex);
        return ResponseEntity.status(exceptionDTO.getHttpStatus()).body(exceptionDTO);
    }

    private boolean hasUpdatedEntityLockException(ExceptionForResponse ex) {
        if (Objects.isNull(ex.getCauseException())) {
            return false;
        }
        return ex.getCauseException() instanceof UpdatedEntityLockException;
    }

    private ResponseEntity<RestExceptionDTO> updatedEntityLockExceptionHandler(UpdatedEntityLockException ex) {
        RestExceptionDTO exceptionDTO = new RestExceptionDTO(ex);
        String message = "RestExceptionDTO: [" + exceptionDTO + "]. Message: [" + ex.getMessage() + "].";
        log.error(message, ex);
        return ResponseEntity.status(exceptionDTO.getHttpStatus()).body(exceptionDTO);
    }

    private void logExceptionMessage(RestExceptionDTO exceptionDTO, ExceptionForResponse ex) {
        StringBuilder message = new StringBuilder();
        message.append("RestExceptionDTO: [").append(exceptionDTO.toString()).append("].");
        if (Objects.nonNull(ex.getCauseException())) {
            message.append(". Message: [").append(ex.getCauseException().getMessage()).append("].");
            log.error(message.toString(), ex.getCauseException());
            return;
        }
        if (Objects.nonNull(ex.getMessage())) {
            message.append(". Message: [").append(ex.getMessage()).append("].");
        }
        log.error(message.toString());
    }

}
