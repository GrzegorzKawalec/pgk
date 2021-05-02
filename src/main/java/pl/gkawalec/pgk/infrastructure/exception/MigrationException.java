package pl.gkawalec.pgk.infrastructure.exception;

public class MigrationException extends RuntimeException {

    public MigrationException(String message) {
        super(message);
    }

    public MigrationException(Throwable cause) {
        super(cause);
    }

}
