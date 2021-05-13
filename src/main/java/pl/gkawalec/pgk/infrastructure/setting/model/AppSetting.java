package pl.gkawalec.pgk.infrastructure.setting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import pl.gkawalec.pgk.infrastructure.setting.model.database.AppDatabaseSetting;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AppSetting {

    public static final String API_PREFIX = "/api";

    private String name = "PGK";
    private String version = "";
    private String author = "";

    private int port = 1003;
    private String timeZone = "UTC";

    private AppDatabaseSetting database = new AppDatabaseSetting();
    private AppSecuritySetting security = new AppSecuritySetting();
    private AppEmailSetting email = new AppEmailSetting();

    public String getApiPrefix() {
        return API_PREFIX;
    }

}
