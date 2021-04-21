package pl.gkawalec.pgk.web.dto.info;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.application.setting.model.AppSetting;

import java.util.Objects;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class InfoBasicDto {

    String name;
    String version;
    String author;

    public static InfoBasicDto of(AppSetting appSetting) {
        Objects.requireNonNull(appSetting, AppSetting.class.getSimpleName() + " cannot be null");
        return InfoBasicDto.builder()
                .name(appSetting.getName())
                .version(appSetting.getVersion())
                .author(appSetting.getAuthor())
                .build();
    }

}
