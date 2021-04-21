package pl.gkawalec.pgk.application.setting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AppMailSetting {

    private String host = "";
    private int port = 0;
    private String login = "";
    private String password = "";

}

