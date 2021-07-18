package pl.gkawalec.pgk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.gkawalec.pgk.api.dto.project.ParticipantDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDataForUpsertDTO;
import pl.gkawalec.pgk.application.project.ProjectService;
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
    public static final String PARTICIPANTS = "/participants";

    private final ProjectService projectService;

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

}
