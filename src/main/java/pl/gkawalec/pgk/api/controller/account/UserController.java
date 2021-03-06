package pl.gkawalec.pgk.api.controller.account;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.gkawalec.pgk.api.dto.account.user.*;
import pl.gkawalec.pgk.application.account.user.UserService;
import pl.gkawalec.pgk.common.annotation.request.AuditedRequest;
import pl.gkawalec.pgk.common.annotation.security.AuthGuard;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.user.LoggedUserAccessor;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppSetting.API_PREFIX + UserController.URL)
public class UserController {

    static final String URL = "/user";
    static final String URL_ME = "/me";
    static final String ACTIVATE = "/activate";
    static final String AUDITING_INFO = "/auditing-info";
    static final String CHANGE_PASSWORD = "/change-password";
    static final String DEACTIVATE = "/deactivate";
    static final String EXISTS_EMAIL = "/exists-email";
    static final String FIND = "/find";
    static final String FIND_UPSERT = "/find-upsert";

    private final UserService userService;
    private final LoggedUserAccessor loggedUserAccessor;

    @GetMapping(URL_ME)
    public UserDTO me() {
        return loggedUserAccessor.getUser();
    }

    @GetMapping(EXISTS_EMAIL)
    public boolean existsUserEmail(@RequestParam("email") String userEmail,
                                   @RequestParam(value = "excludedId", required = false) Integer excludedUserId) {
        return userService.existsUserEmail(userEmail, excludedUserId);
    }

    @GetMapping(FIND_UPSERT + "/{id}")
    public UserUpsertDTO findUpsertById(@PathVariable("id") Integer userId) {
        return userService.findUpsertById(userId);
    }

    @PostMapping
    @AuthGuard({Authority.USER_WRITE, Authority.ROLE_WRITE})
    public UserUpsertDTO create(@RequestBody UserUpsertDTO dto) {
        return userService.create(dto);
    }

    @PutMapping
    @AuthGuard({Authority.USER_WRITE, Authority.ROLE_WRITE})
    public UserUpsertDTO update(@RequestBody UserUpsertDTO dto) {
        return userService.update(dto);
    }

    @PostMapping(FIND)
    @AuditedRequest(false)
    public Page<UserSearchDTO> find(@RequestBody(required = false) UserCriteria criteria) {
        return userService.find(criteria);
    }

    @AuditedRequest
    @GetMapping(AUDITING_INFO + "/{id}")
    @AuthGuard({Authority.USER_READ, Authority.USER_WRITE, Authority.ROLE_READ, Authority.ROLE_WRITE})
    public UserAuditingDTO getAuditingInfo(@PathVariable("id") Integer userId) {
        return userService.getAuditingInfo(userId);
    }

    @PutMapping(DEACTIVATE + "/{id}")
    @AuthGuard({Authority.USER_WRITE, Authority.ROLE_WRITE})
    public void deactivate(@PathVariable("id") Integer userId) {
        userService.deactivate(userId);
    }

    @PutMapping(ACTIVATE + "/{id}")
    @AuthGuard({Authority.USER_WRITE, Authority.ROLE_WRITE})
    public void activate(@PathVariable("id") Integer userId) {
        userService.activate(userId);
    }

    @PostMapping(CHANGE_PASSWORD)
    public void changePassword(@RequestBody UserChangePasswordDTO dto) {
        userService.changePassword(dto);
    }

}
