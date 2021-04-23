package pl.gkawalec.pgk.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gkawalec.pgk.application.info.InfoService;
import pl.gkawalec.pgk.web.dto.info.InfoBasicDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/basic")
    public InfoBasicDto getBasicInfo() {
        return infoService.getBasicInfo();
    }

}
