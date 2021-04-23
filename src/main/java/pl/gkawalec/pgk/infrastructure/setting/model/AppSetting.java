package pl.gkawalec.pgk.infrastructure.setting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AppSetting {

    private String name = "PGK";
    private String version = "";
    private String profile = "";
    private String author = "";

    private int port = 1003;
    private String timeZone = "UTC";

    private String contextPath = "";
    private String apiPrefix = "/api";

    private AppDatabaseSetting database = new AppDatabaseSetting();
    private AppSecuritySetting security = new AppSecuritySetting();
    private AppMailSetting mail = new AppMailSetting();

}
