package pl.gkawalec.pgk.infrastructure.setting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AppSecuritySetting {

    private String[] unauthorizedRequests = new String[0];

}
