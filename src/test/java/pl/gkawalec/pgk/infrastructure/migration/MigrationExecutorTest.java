package pl.gkawalec.pgk.infrastructure.migration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.common.exception.MigrationException;
import pl.gkawalec.pgk.database.migration.FlywayMigrationsRepository;
import pl.gkawalec.pgk.database.migration.MigrationEntity;
import pl.gkawalec.pgk.database.migration.MigrationEntityMapper;
import pl.gkawalec.pgk.database.migration.MigrationRepository;
import pl.gkawalec.pgk.testconfig.annotation.PGKSpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@PGKSpringBootTest
class MigrationExecutorTest {

    private int initVersion = 1;
    private final long initExecutionTime = 10L;
    private final String initDesc = "desc desc  desc";

    @Autowired
    private MigrationExecutor executor;

    @Autowired
    private MigrationRepository migrationRepository;

    @Autowired
    private FlywayMigrationsRepository flywayRepository;

    @Autowired
    private MigrationVersionHelper migrationVersionHelper;

    private MigrationEntity initTestEntity;

    @BeforeEach
    void setUp() {
        PGKMigration initTestMigration = Mockito.mock(TestPGKMigration.class, Mockito.CALLS_REAL_METHODS);
        initVersion = migrationVersionHelper.getFirstAvailableVersion();
        ReflectionTestUtils.setField(initTestMigration, "version", initVersion);
        ReflectionTestUtils.setField(initTestMigration, "description", initDesc);
        ReflectionTestUtils.setField(initTestMigration, "executionTimeMs", initExecutionTime);
        MigrationEntity entity = MigrationEntityMapper.create(initTestMigration);
        initTestEntity = migrationRepository.save(entity);
    }

    @AfterEach
    void tearDown() {
        migrationRepository.delete(initTestEntity);
    }

    @Test
    @DisplayName("Check if the migration was successful")
    void execute_Succeed() {
        //given
        PGKMigration migration = Mockito.mock(TestPGKMigration.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(migration, "version", initVersion + 1);
        ReflectionTestUtils.setField(migration, "description", initDesc);
        ReflectionTestUtils.setField(migration, "executionTimeMs", initExecutionTime);

        //when
        executor.execute(migration);

        //then
        assertFalse(migration.migrationWasSkipped(), "Migration should be done.");
    }

    @Test
    @DisplayName("Check if the migration will be skipped because the version already exists in the database")
    void execute_SkippedExistVersion() {
        //given
        PGKMigration migration = Mockito.mock(TestPGKMigration.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(migration, "version", initVersion);
        ReflectionTestUtils.setField(migration, "description", initDesc);
        ReflectionTestUtils.setField(migration, "executionTimeMs", initExecutionTime);

        //when
        executor.execute(migration);

        //then
        assertTrue(migration.migrationWasSkipped(), "Migration should be skipped as the version exists in the database.");
    }

    @Test
    @DisplayName("Verify that the migration will be skipped because the version already exists in the database and has a different description")
    void execute_SkippedDifferentDescription() {
        //given
        PGKMigration migration = Mockito.mock(TestPGKMigration.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(migration, "version", initVersion);
        ReflectionTestUtils.setField(migration, "description", initDesc + initDesc);
        ReflectionTestUtils.setField(migration, "executionTimeMs", initExecutionTime);

        //when
        MigrationException migrationException = assertThrows(MigrationException.class, () -> executor.execute(migration));

        //then
        assertNotNull(migrationException, "Execution must throw an exception because the version exists in the database and has a different description.");
        assertTrue(migration.migrationWasSkipped(), "Migration should be skipped because the version exists in the database and has a different description.");
    }

    @Test
    @DisplayName("Check if the migration will be skipped as it differs by more than 1 from the last migration version")
    void execute_SkippedDifferenceBetweenLastVersion() {
        //given
        PGKMigration migration = Mockito.mock(TestPGKMigration.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(migration, "version", initVersion + 10);
        ReflectionTestUtils.setField(migration, "description", initDesc);
        ReflectionTestUtils.setField(migration, "executionTimeMs", initExecutionTime);

        //when
        MigrationException migrationException = assertThrows(MigrationException.class, () -> executor.execute(migration));

        //then
        assertNotNull(migrationException, "Execution must throw an exception because it cannot differ by more than 1 from the last one.");
        assertTrue(migration.migrationWasSkipped(), "Migration should be skipped because the version is different than 1 from the existing one.");
    }

    @Test
    @DisplayName("Check if the migration will be skipped as there is no required Flyway migration in the database")
    void execute_SkippedWithoutRequiredFlywayMigration() {
        //given
        PGKMigration migration = Mockito.mock(TestPGKMigration.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(migration, "version", initVersion + 1);
        ReflectionTestUtils.setField(migration, "description", initDesc);
        ReflectionTestUtils.setField(migration, "executionTimeMs", initExecutionTime);
        Set<String> notExistFlywayVersion = getNotExistFlywayVersion();

        //when
        when(migration.requiredFlywayMigrationVersions()).thenReturn(notExistFlywayVersion);
        MigrationException migrationException = assertThrows(MigrationException.class, () -> executor.execute(migration));

        //then
        assertNotNull(migrationException, "Execution must throw an exception because the required migration does not exist in the database.");
        assertTrue(migration.migrationWasSkipped(), "Migration should be skipped because the required migration does not exist in the database.");
    }

    private Set<String> getNotExistFlywayVersion() {
        int versionNumber = 1_000_000_000;
        while (flywayRepository.existsByVersion(versionNumber + "")) {
            versionNumber++;
        }
        return Set.of(versionNumber + "");
    }

    private static class TestPGKMigration extends PGKMigration {

        @Override
        protected void executeMigration() {

        }
    }


}
