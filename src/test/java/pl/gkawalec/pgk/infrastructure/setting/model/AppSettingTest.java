package pl.gkawalec.pgk.infrastructure.setting.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.infrastructure.setting.model.database.AppDatabaseMigrationsSetting;
import pl.gkawalec.pgk.infrastructure.setting.model.database.AppDatabaseSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PGKSpringBootTest
class AppSettingTest {

    @Autowired
    private AppSetting appSetting;

    @Test
    @DisplayName("Check required application settings")
     void testRequiredAppSettings() {
        //when
        String name = appSetting.getName();
        String version = appSetting.getVersion();
        int port = appSetting.getPort();
        String timeZone = appSetting.getTimeZone();
        String apiPrefix = AppSetting.API_PREFIX;
        AppDatabaseSetting databaseSetting = appSetting.getDatabase();
        AppSecuritySetting securitySetting = appSetting.getSecurity();
        AppEmailSetting mailSetting = appSetting.getEmail();

        //then
        assertTrue(StringUtils.isNotBlank(name), "App name is required");
        assertTrue(StringUtils.isNotBlank(version), "App version is required");
        assertTrue(port > 1_000, "App port must be grater than 1 000");
        assertNotNull(TimeZone.getTimeZone(timeZone), "Set time zone is not available");
        assertTrue(StringUtils.isNotBlank(apiPrefix), "Api prefix is required");
        assertNotNull(databaseSetting, "Database setting cannot be null");
        assertNotNull(securitySetting, "Security setting cannot be null");
        assertNotNull(mailSetting, "Email setting cannot be null");
    }

    @Test
    @DisplayName("Check required database settings")
    void testRequiredDatabaseSettings() {
        //given
        AppDatabaseSetting databaseSetting = appSetting.getDatabase();

        //when
        int maxPoolSize = databaseSetting.getMaxPoolSize();
        String schema = databaseSetting.getSchema();
        AppDatabaseMigrationsSetting migrations = databaseSetting.getMigrations();

        //then
        assertTrue(maxPoolSize > 0, "Database max pool size must be grater than 0");
        assertTrue(StringUtils.isNotBlank(schema), "Database schema is required");
        assertNotNull(migrations, "Database migrations setting cannot be null");
    }

    @Test
    @DisplayName("Check required database migrations settings")
    void testRequiredDatabaseMigrationsSettings() {
        //given
        AppDatabaseMigrationsSetting migrations = appSetting.getDatabase().getMigrations();

        //when
        String prefix = migrations.getPrefix();
        String separator = migrations.getSeparator();

        //then
        assertTrue(StringUtils.isNotBlank(prefix), "Migration prefix is required");
        assertTrue(StringUtils.isNotBlank(separator), "Migration separator is required");
    }

    @Test
    @DisplayName("Check required security settings")
    void testRequiredSecuritySettings() {
        //given
        AppSecuritySetting securitySetting = appSetting.getSecurity();

        //when
        String[] unauthorizedRequests = securitySetting.getUnauthorizedRequests();

        //then
        assertNotNull(unauthorizedRequests, "Unauthorized request array cannot be null");
    }

    @Test
    @DisplayName("Check required email settings")
    void testRequiredMailSettings() {
        //given
        AppEmailSetting mailSetting = appSetting.getEmail();

        //when
        String host = mailSetting.getHost();
        int port = mailSetting.getPort();
        String login = mailSetting.getLogin();
        String password = mailSetting.getPassword();

        //then
        assertTrue(StringUtils.isNotBlank(host), "Mail host is required");
        assertTrue(port > 0, "Mail port must be grater than 0");
        assertTrue(StringUtils.isNotBlank(login), "Mail login is required");
        assertTrue(StringUtils.isNotBlank(password), "Mail password is required");
    }


}
