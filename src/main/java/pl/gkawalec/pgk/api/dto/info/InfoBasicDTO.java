package pl.gkawalec.pgk.api.dto.info;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class InfoBasicDTO {

    String name;
    String version;
    String author;

    public static InfoBasicDTO of(AppSetting appSetting) {
        return InfoBasicDTO.builder()
                .name(appSetting.getName())
                .version(appSetting.getVersion())
                .author(appSetting.getAuthor())
                .build();
    }

}
