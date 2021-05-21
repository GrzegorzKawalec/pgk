package pl.gkawalec.pgk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gkawalec.pgk.api.dto.user.UserDTO;
import pl.gkawalec.pgk.common.user.LoggedUserAccessor;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppSetting.API_PREFIX + UserController.URL)
public class UserController {

    public static final String URL = "/user";
    public static final String URL_ME = "/me";

    private final LoggedUserAccessor loggedUserAccessor;

    @GetMapping(URL_ME)
    public UserDTO me() {
        return loggedUserAccessor.getUser();
    }

}
