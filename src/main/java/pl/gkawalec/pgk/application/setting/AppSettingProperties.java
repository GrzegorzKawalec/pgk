package pl.gkawalec.pgk.application.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.gkawalec.pgk.application.setting.model.AppSetting;

@Configuration
class AppSettingProperties {

    @Bean
    @ConfigurationProperties("app")
    AppSetting appStaticSetting() {
        return new AppSetting();
    }

}
