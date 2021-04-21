package pl.gkawalec.pgk.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import pl.gkawalec.pgk.application.setting.model.AppSetting;

import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class TimeZoneConfig {

    private final AppSetting appSetting;

    @Autowired
    private void setTimeZone() {
        String timeZone = appSetting.getTimeZone();
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        System.setProperty("user.timezone", timeZone);
    }

}
