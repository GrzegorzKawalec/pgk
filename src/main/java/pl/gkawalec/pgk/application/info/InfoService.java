package pl.gkawalec.pgk.application.info;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.web.dto.info.InfoBasicDto;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final AppSetting appSetting;

    public InfoBasicDto getBasicInfo() {
        return InfoBasicDto.of(appSetting);
    }

}
