package pl.gkawalec.pgk.application.setting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AppDatabaseSetting {

    private boolean p6spy = true;
    private int maxPoolSize = 10;

}
