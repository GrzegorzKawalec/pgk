package pl.gkawalec.pgk.infrastructure.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.utils.CollectionUtil;
import pl.gkawalec.pgk.common.utils.StringUtil;
import pl.gkawalec.pgk.database.migration.MigrationEntity;
import pl.gkawalec.pgk.database.migration.MigrationEntityMapper;
import pl.gkawalec.pgk.database.migration.MigrationRepository;
import pl.gkawalec.pgk.infrastructure.exception.MigrationException;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "PGKMigrations")
@ComponentScan(includeFilters = @ComponentScan.Filter(PGKMigrationAnnotation.class), value = {"migrations"})
class MigrationRunner {

    private final List<PGKMigration> migrations;

    private final MigrationRepository repository;

    private final MigrationExecutor executor;

    private final ApplicationContext applicationContext;

    @PostConstruct
    public void runMigrations() {
        log.info("Start of PGKMigrations");
        List<PGKMigration> migrations = filterMigrations();
        if (CollectionUtil.isEmpty(migrations)) {
            log.info("Migrations not found");
        } else {
            migrations.forEach(this::runSingleMigration);
            migrations.clear();
        }
        log.info("End of PGKMigrations");
    }

    @EventListener(ApplicationStartedEvent.class)
    public void destroyMigrationsBeansAfterApplicationStarted() {
        destroyMigrationsBeans();
    }

    private List<PGKMigration> filterMigrations() {
        if (CollectionUtil.isEmpty(migrations)) {
            return Collections.emptyList();
        }
        return migrations.stream()
                .filter(PGKMigration::isValidName)
                .sorted(Comparator.comparing(PGKMigration::getVersion))
                .collect(Collectors.toList());
    }

    private void runSingleMigration(PGKMigration migration) {
        MigrationException exceptionOccurred = executeOrGetException(migration);
        if (Objects.nonNull(exceptionOccurred)) {
            log.error("Migration version '" + migration.getVersion() + "' has failed. Exception message: " + exceptionOccurred.getMessage());
            throw exceptionOccurred;
        }
        saveHistoryMigration(migration);
    }

    private MigrationException executeOrGetException(PGKMigration migration) {
        try {
            executor.execute(migration);
            return null;
        } catch (Exception ex) {
            return getOrCastMigrationException(ex);
        }
    }

    private MigrationException getOrCastMigrationException(Exception ex) {
        return ex instanceof MigrationException ?
                (MigrationException) ex :
                new MigrationException(ex);
    }

    private void saveHistoryMigration(PGKMigration migration) {
        if (migration.migrationWasSkipped()) {
            log.info("Migration version '" + migration.getVersion() + "' skipped");
            return;
        }
        MigrationEntity migrationHistoryEntity = MigrationEntityMapper.create(migration);
        repository.save(migrationHistoryEntity);
        String migrationLogMessage = "Migrated to version \"" + migration.getVersion() + ": " + migration.getDescription() + "\"";
        log.info(migrationLogMessage);
    }

    private void destroyMigrationsBeans() {
        BeanDefinitionRegistry factory = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        List<String> migrationBeanNamesForDestruct = getMigrationBeanNamesFoDestruct();
        migrations.clear();
        migrationBeanNamesForDestruct.forEach(factory::removeBeanDefinition);
    }

    private List<String> getMigrationBeanNamesFoDestruct() {
        return migrations.stream().map(migration -> {
            String beanClassName = migration.getClass().getSimpleName();
            return StringUtil.changeFirstLetterToLowercase(beanClassName);
        }).collect(Collectors.toList());
    }

}
