package pl.gkawalec.pgk.application.setting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AppSetting {

    private String name = "PGK";
    private String version = "";
    private String author = "";

    private int port = 1003;

    private String contextPath = "";
    private String apiPrefix = "/api";

    private AppDatabaseSetting database = new AppDatabaseSetting();

}
