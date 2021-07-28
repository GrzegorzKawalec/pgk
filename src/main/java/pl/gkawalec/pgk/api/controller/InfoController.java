package pl.gkawalec.pgk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gkawalec.pgk.application.info.InfoService;
import pl.gkawalec.pgk.api.dto.info.InfoBasicDTO;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppSetting.API_PREFIX + InfoController.URL)
public class InfoController {

    static final String URL = "/info";
    static final String URL_BASIC = "/basic";

    private final InfoService infoService;

    @GetMapping(URL_BASIC)
    public InfoBasicDTO getBasicInfo() {
        return infoService.getBasicInfo();
    }

}
