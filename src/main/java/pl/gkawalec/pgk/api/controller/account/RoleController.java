package pl.gkawalec.pgk.api.controller.account;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.gkawalec.pgk.api.dto.account.role.RoleAuditingDTO;
import pl.gkawalec.pgk.api.dto.account.role.RoleCriteria;
import pl.gkawalec.pgk.api.dto.account.role.RoleDTO;
import pl.gkawalec.pgk.application.account.role.RoleService;
import pl.gkawalec.pgk.common.annotation.request.AuditedRequest;
import pl.gkawalec.pgk.common.annotation.security.AuthGuard;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppSetting.API_PREFIX + RoleController.URL)
public class RoleController {

    public static final String URL = "/role";
    public static final String AUDITING_INFO = "/auditing-info";
    public static final String AUTHORITIES = "/authorities";
    public static final String EXISTS_NAME = "/exists-name";
    public static final String FIND = "/find";

    private final RoleService service;

    @GetMapping(RoleController.AUTHORITIES)
    @AuthGuard({Authority.ROLE_READ, Authority.ROLE_WRITE})
    public Set<Authority> getAllAuthorities() {
        return Set.of(Authority.values());
    }

    @GetMapping(RoleController.EXISTS_NAME)
    public boolean existsName(@RequestParam("name") String roleName,
                              @RequestParam(value = "id", required = false) Integer roleId) {
        return service.existsName(roleName, roleId);
    }

    @GetMapping("{id}")
    @AuthGuard(Authority.ROLE_READ)
    public RoleDTO findById(@PathVariable("id") Integer roleId) {
        return service.findById(roleId);
    }

    @PostMapping
    @AuthGuard(Authority.ROLE_WRITE)
    public RoleDTO create(@RequestBody RoleDTO dto) {
        return service.create(dto);
    }

    @PutMapping
    @AuthGuard(Authority.ROLE_WRITE)
    public RoleDTO update(@RequestBody RoleDTO dto) {
        return service.update(dto);
    }

    @AuditedRequest(false)
    @AuthGuard(Authority.ROLE_READ)
    @PostMapping(RoleController.FIND)
    public Page<RoleDTO> find(@RequestBody(required = false) RoleCriteria criteria) {
        return service.find(criteria);
    }

    @AuthGuard(Authority.ROLE_READ)
    @GetMapping(RoleController.AUDITING_INFO + "/{id}")
    public RoleAuditingDTO getAuditingInfo(@PathVariable("id") Integer roleId) {
        return service.getAuditingInfo(roleId);
    }

    @DeleteMapping("{id}")
    @AuthGuard(Authority.ROLE_WRITE)
    public void delete(@PathVariable("id") Integer roleId) {
        service.delete(roleId);
    }

}
