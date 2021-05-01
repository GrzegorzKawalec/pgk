package pl.gkawalec.pgk.application.info;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.api.dto.info.InfoBasicDTO;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final AppSetting appSetting;

    public InfoBasicDTO getBasicInfo() {
        return InfoBasicDTO.of(appSetting);
    }

}
