package pl.gkawalec.pgk.infrastructure.migration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.infrastructure.setting.model.database.AppDatabaseMigrationsSetting;
import pl.gkawalec.pgk.infrastructure.setting.model.database.AppDatabaseSetting;
import pl.gkawalec.pgk.testconfig.annotation.PGKSpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@PGKSpringBootTest
class PGKMigrationTest {

    private final static String SEPARATOR = "__";
    private final static String PREFIX = "V";

    private final PGKMigration testMigration = Mockito.mock(TestPGKMigration.class, Mockito.CALLS_REAL_METHODS);
    private final AppSetting appSetting = Mockito.mock(AppSetting.class);

    @BeforeEach
    void setUp() {
        AppDatabaseSetting appDatabaseSetting = Mockito.mock(AppDatabaseSetting.class);
        AppDatabaseMigrationsSetting appDatabaseMigrationsSetting = Mockito.mock(AppDatabaseMigrationsSetting.class);
        when(appSetting.getDatabase()).thenReturn(appDatabaseSetting);
        when(appSetting.getDatabase().getMigrations()).thenReturn(appDatabaseMigrationsSetting);
        when(appSetting.getDatabase().getMigrations().getSeparator()).thenReturn(SEPARATOR);
        when(appSetting.getDatabase().getMigrations().getPrefix()).thenReturn(PREFIX);
        testMigration.setAppSetting(appSetting);
    }

    @Test
    @DisplayName("Check correct migration name")
    void correctMigrationName() {
        //given
        String name = "V001___desc_desc__desc__";

        //when

        ReflectionTestUtils.setField(testMigration, "migrationName", name);

        //then
        assertTrue(testMigration.isValidName(), "This name is correct.");
        assertEquals(1, testMigration.getVersion());
        assertEquals("desc desc  desc", testMigration.getDescription());
    }

    @Test
    @DisplayName("Check for invalid migration name (different separator)")
    void incorrectMigrationName_DifferentSeparator() {
        //given
        String name = "V001_desc";

        //when
        ReflectionTestUtils.setField(testMigration, "migrationName", name);

        //then
        assertFalse(testMigration.isValidName(), "This is an invalid name. Different separator.");
    }

    @Test
    @DisplayName("Check for invalid migration name (different prefix)")
    void incorrectMigrationName_DifferentPrefix() {
        //given
        String name = "v001__desc_desc__desc__";

        //when
        ReflectionTestUtils.setField(testMigration, "migrationName", name);

        //then
        assertFalse(testMigration.isValidName(), "This is an invalid name. Different prefix.");
    }

    @Test
    @DisplayName("Check for invalid migration name (version is not a number)")
    void incorrectMigrationName_VersionIsNotNumber() {
        //given
        String name = "V1a__desc";

        //when
        ReflectionTestUtils.setField(testMigration, "migrationName", name);

        //then
        assertFalse(testMigration.isValidName(), "This is an invalid name. Version is not a number.");
    }

    @Test
    @DisplayName("Check for invalid migration name (version is not a positive number)")
    void incorrectMigrationName_VersionIsNotPositiveNumber() {
        //given
        String name = "V0__desc";

        //when
        ReflectionTestUtils.setField(testMigration, "migrationName", name);

        //then
        assertFalse(testMigration.isValidName(), "This is an invalid name. Version is not a positive number.");
    }

    @Test
    @DisplayName("Check for invalid migration name (without version)")
    void incorrectMigrationName_WithoutVersion() {
        //given
        String name = "V__desc";

        //when
        ReflectionTestUtils.setField(testMigration, "migrationName", name);

        //then
        assertFalse(testMigration.isValidName(), "This is an invalid name. Without version.");
    }

    @Test
    @DisplayName("Check for invalid migration name (without description)")
    void incorrectMigrationName_WithoutDescription() {
        //given
        String name = "V1__";

        //when
        ReflectionTestUtils.setField(testMigration, "migrationName", name);

        //then
        assertFalse(testMigration.isValidName(), "This is an invalid name. Without description.");
    }

    private static class TestPGKMigration extends PGKMigration {
        @Override
        protected void executeMigration() {
        }
    }

}
