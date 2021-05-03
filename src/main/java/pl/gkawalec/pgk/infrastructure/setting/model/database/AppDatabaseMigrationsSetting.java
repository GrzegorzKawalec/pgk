package pl.gkawalec.pgk.infrastructure.setting.model.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AppDatabaseMigrationsSetting {

    private String prefix;
    private String separator;

}
