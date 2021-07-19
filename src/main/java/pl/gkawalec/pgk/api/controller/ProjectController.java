package pl.gkawalec.pgk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.gkawalec.pgk.api.dto.common.SelectDTO;
import pl.gkawalec.pgk.api.dto.project.ParticipantDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectCriteria;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDataForUpsertDTO;
import pl.gkawalec.pgk.application.account.user.UserService;
import pl.gkawalec.pgk.application.legalact.LegalActService;
import pl.gkawalec.pgk.application.project.ProjectService;
import pl.gkawalec.pgk.common.annotation.request.AuditedRequest;
import pl.gkawalec.pgk.common.annotation.security.AuthGuard;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppSetting.API_PREFIX + ProjectController.URL)
public class ProjectController {

    public static final String URL = "/project";
    public static final String DATA_FOR_UPSERT = "/data-for-upsert";
    public static final String FIND = "/find";
    public static final String PARTICIPANTS = "/participants";
    public static final String SELECT_LEGAL_ACTS = "/select-legal-acts";
    public static final String SELECT_PARTICIPANTS = "/select-participants";

    private final UserService userService;
    private final ProjectService projectService;
    private final LegalActService legalActService;

    @AuthGuard(Authority.PROJECT_WRITE)
    @GetMapping(DATA_FOR_UPSERT + "/{id}")
    public ProjectDataForUpsertDTO findForUpsertById(@PathVariable("id") Long projectId) {
        return projectService.findForUpsertById(projectId);
    }

    @PostMapping
    @AuthGuard(Authority.PROJECT_WRITE)
    public ProjectDTO create(@RequestBody ProjectDTO dto) {
        return projectService.create(dto);
    }

    @PutMapping
    @AuthGuard(Authority.PROJECT_WRITE)
    public ProjectDTO update(@RequestBody ProjectDTO dto) {
        return projectService.update(dto);
    }

    @GetMapping(PARTICIPANTS)
    public List<ParticipantDTO> getParticipants() {
        return projectService.getParticipants();
    }

    @GetMapping(SELECT_LEGAL_ACTS)
    public List<SelectDTO> getSelectLegalActs() {
        return legalActService.getSelectLegalActs();
    }

    @GetMapping(SELECT_PARTICIPANTS)
    public List<SelectDTO> getSelectParticipants() {
        return userService.getSelectedUsers();
    }

    @PostMapping(FIND)
    @AuditedRequest(false)
    public Page<ProjectDTO> find(@RequestBody(required = false) ProjectCriteria criteria) {
        return projectService.find(criteria);
    }

}
