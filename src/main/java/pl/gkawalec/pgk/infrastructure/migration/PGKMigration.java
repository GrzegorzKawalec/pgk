package pl.gkawalec.pgk.infrastructure.migration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.common.exception.MigrationException;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@PGKMigrationAnnotation
public abstract class PGKMigration {

    protected abstract void executeMigration();

    /**
     * If a Flyway migration is required in the database, override the method that returns a set of required versions.
     * @return Set with the required versions of the Flyway migration.
     */
    protected Set<String> requiredFlywayMigrationVersions() {
        return Collections.emptySet();
    }



    private final String migrationName = this.getClass().getSimpleName();

    private Boolean isValidName;

    @Getter
    private Integer version;
    @Getter
    private String description;

    @Getter
    private Long executionTimeMs;
    private Instant startedAt;

    private boolean migrationWasSkipped = false;

    private AppSetting appSetting;

    public final boolean isValidName() {
        if (Objects.nonNull(isValidName)) return isValidName;
        isValidName = validAndInit();
        return isValidName;
    }

    @Autowired
    final void setAppSetting(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    final boolean migrationWasSkipped() {
        return migrationWasSkipped;
    }

    final void migrationSkipped() {
        this.migrationWasSkipped = true;
    }

    final void execute() {
        startedAt = Instant.now();
        MigrationException exceptionOccurred = null;
        try {
            executeMigration();
        } catch (Exception ex) {
            exceptionOccurred = new MigrationException(ex);
        }
        calculateExecutionTime();
        if (Objects.nonNull(exceptionOccurred)) {
            throw exceptionOccurred;
        }
    }

    private void calculateExecutionTime() {
        Instant now = Instant.now();
        Duration duration = Duration.between(startedAt, now);
        executionTimeMs = duration.toMillis();
    }

    private boolean validAndInit() {
        String prefix = getMigrationPrefix();
        String separator = getMigrationSeparator();

        if (notStartWithPrefix(prefix)) return false;
        if (notContainsSeparator(separator)) return false;

        String[] splitMigrationName = migrationName.split(separator);

        return validateAndInitVersion(splitMigrationName, prefix) &&
                validateAndInitDescription(splitMigrationName, separator);
    }

    private String getMigrationPrefix() {
        return appSetting.getDatabase().getMigrations().getPrefix();
    }

    private String getMigrationSeparator() {
        return appSetting.getDatabase().getMigrations().getSeparator();
    }

    private boolean notStartWithPrefix(String prefix) {
        return !migrationName.startsWith(prefix);
    }

    private boolean notContainsSeparator(String separator) {
        return !migrationName.contains(separator);
    }

    private boolean validateAndInitVersion(String[] splitMigrationName, String prefix) {
        try {
            String versionStr = splitMigrationName[0].substring(prefix.length());
            version = Integer.parseInt(versionStr);
            if (version < 1) {
                version = null;
                return false;
            }
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean validateAndInitDescription(String[] splitMigrationName, String separator) {
        String delimiter = " ".repeat(separator.length());
        StringJoiner descriptionJoiner = new StringJoiner(delimiter);
        for (int i = 1; i < splitMigrationName.length; i++) {
            String partOfDescription = splitMigrationName[i].replace("_", " ");
            descriptionJoiner.add(partOfDescription);
        }

        String description = StringUtil.trim(descriptionJoiner.toString());
        if (StringUtil.isBlank(description)) return false;

        this.description = description;
        return true;
    }

}
