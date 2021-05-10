package pl.gkawalec.pgk.common.exception;

import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

public class UpdatedEntityLockException extends RuntimeException {

    public UpdatedEntityLockException(Class<? extends AuditingEntity> clazz, int oldVersion, int newVersion) {
        super(
                UpdatedEntityLockException.buildMessage(clazz, oldVersion, newVersion)
        );
    }

    private static String buildMessage(Class<? extends AuditingEntity> clazz, int entityVersion, int newVersion) {
        return "Entity " + clazz.getSimpleName() + " version is higher than the new version." +
                " Entity version: " + entityVersion + "." +
                " New version: " + newVersion + ".";
    }

}
