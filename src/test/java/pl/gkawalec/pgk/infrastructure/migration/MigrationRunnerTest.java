package pl.gkawalec.pgk.infrastructure.migration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.database.migration.MigrationRepository;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PGKSpringBootTest
class MigrationRunnerTest {

    @Autowired
    private MigrationRunner migrationRunner;

    @Autowired
    private AppSetting appSetting;

    @Autowired
    private MigrationRepository migrationRepository;

    @Autowired
    private MigrationVersionHelper migrationVersionHelper;

    @Test
    @Transactional
    @DisplayName("Migration run test")
    void runMigrations() {
        //given
        int version = migrationVersionHelper.getFirstAvailableVersion();
        PGKMigration migration_1 = mockMigration(TestPGKMigration_1.class, version++, true);
        PGKMigration migration_2 = mockMigration(TestPGKMigration_2.class, version++, true);
        PGKMigration migration_incorrect = mockMigration(TestPGKMigration_3.class, version, false);
        List<PGKMigration> migrations = new ArrayList<>();
        migrations.add(migration_1);
        migrations.add(migration_2);
        migrations.add(migration_incorrect);
        ReflectionTestUtils.setField(migrationRunner, "migrations", migrations);

        long countBeforeRun = migrationRepository.count();

        //when
        migrationRunner.runMigrations();
        long countAfterRun = migrationRepository.count();
        long diffAfterRun = countAfterRun - countBeforeRun;

        //then
        assertEquals(2, diffAfterRun, "2 migrations were correctly defined, and there is " + diffAfterRun + " in the database after execution");
    }

    private PGKMigration mockMigration(Class<? extends PGKMigration> migrationClass, int version, boolean correctName) {
        String prefix = appSetting.getDatabase().getMigrations().getPrefix();
        String separator = appSetting.getDatabase().getMigrations().getSeparator();
        String description = "desc";
        String migrationName = version + separator + description;
        if (correctName) {
            migrationName = prefix + migrationName;
        }
        PGKMigration migration = Mockito.mock(migrationClass, Mockito.CALLS_REAL_METHODS);
        migration.setAppSetting(appSetting);
        ReflectionTestUtils.setField(migration, "migrationName", migrationName);
        return migration;
    }

    private static class TestPGKMigration_1 extends PGKMigration {
        @Override
        protected void executeMigration() {
        }
    }

    private static class TestPGKMigration_2 extends PGKMigration {
        @Override
        protected void executeMigration() {
        }
    }

    private static class TestPGKMigration_3 extends PGKMigration {
        @Override
        protected void executeMigration() {
        }
    }

}
