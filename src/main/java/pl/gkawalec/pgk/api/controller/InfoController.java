package pl.gkawalec.pgk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gkawalec.pgk.application.info.InfoService;
import pl.gkawalec.pgk.api.dto.info.InfoBasicDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/basic")
    public InfoBasicDTO getBasicInfo() {
        return infoService.getBasicInfo();
    }

}
