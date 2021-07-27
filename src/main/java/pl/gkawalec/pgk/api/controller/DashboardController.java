package pl.gkawalec.pgk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gkawalec.pgk.api.dto.dashboard.DashboardDTO;
import pl.gkawalec.pgk.application.dashboard.DashboardService;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppSetting.API_PREFIX + DashboardController.URL)
public class DashboardController {

    public static final String URL = "/dashboard";

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboard();
    }

}
