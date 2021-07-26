package pl.gkawalec.pgk.application.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.database.project.ProjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class ProjectUpdater {

    private final ProjectHelper helper;

    void update(ProjectEntity projectEntity, ProjectDTO dto) {
        ProjectMapper.update(dto, projectEntity);
        updateProjectManager(projectEntity, dto);
        updateParticipants(projectEntity, dto);
        updateLegalActs(projectEntity, dto);
    }

    private void updateProjectManager(ProjectEntity projectEntity, ProjectDTO dto) {
        Integer currentProjectManagerId = projectEntity.getProjectManager().getId();
        Integer newProjectManagerId = dto.getProjectManager().getId();
        if (currentProjectManagerId.equals(newProjectManagerId)) {
            return;
        }
        UserEntity projectManager = helper.findProjectManager(dto);
        ProjectMapper.updateProjectManager(projectEntity, projectManager);
    }

    private void updateParticipants(ProjectEntity projectEntity, ProjectDTO dto) {
        List<UserEntity> currentParticipants = projectEntity.getParticipants();
        Set<Number> newParticipantIds = dto.getParticipants().stream()
                .map(UserDTO::getId)
                .collect(Collectors.toSet());
        List<UserEntity> newParticipants = getForUpdateOrEmptyListIfIsTheSame(
                currentParticipants,
                newParticipantIds,
                UserEntity::getId,
                helper::findParticipants
        );
        if (CollectionUtil.isNotEmpty(newParticipants)) {
            ProjectMapper.updateParticipants(projectEntity, newParticipants);
        }
    }

    void updateLegalActs(ProjectEntity projectEntity, ProjectDTO dto) {
        List<LegalActEntity> currentLegalActs = projectEntity.getLegalActs();
        Set<Number> newLegalActIds = dto.getLegalActs().stream()
                .map(LegalActDTO::getId)
                .collect(Collectors.toSet());
        List<LegalActEntity> newLegalActs = getForUpdateOrEmptyListIfIsTheSame(
                currentLegalActs,
                newLegalActIds,
                LegalActEntity::getId,
                helper::findLegalActs
        );
        if (CollectionUtil.isNotEmpty(newLegalActs)) {
            ProjectMapper.updateLegalActs(projectEntity, newLegalActs);
        }
    }

    private <R> List<R> getForUpdateOrEmptyListIfIsTheSame(List<R> entities, Set<Number> ids,
                                                           Function<R, Number> getIdFromEntityFunction,
                                                           Function<Set<Number>, List<R>> findEntitiesFunction) {
        List<R> filteredEntities = entities.stream()
                .filter(e -> ids.contains(getIdFromEntityFunction.apply(e)))
                .collect(Collectors.toList());
        if (filteredEntities.size() == entities.size() && filteredEntities.size() == ids.size()) {
            return Collections.emptyList();
        }
        Set<Number> entityIds = entities.stream()
                .map(getIdFromEntityFunction)
                .collect(Collectors.toSet());
        Set<Number> idForFind = ids.stream()
                .filter(id -> !entityIds.contains(id))
                .collect(Collectors.toSet());
        List<R> entitiesForAdd = findEntitiesFunction.apply(idForFind);
        filteredEntities.addAll(entitiesForAdd);
        return filteredEntities;
    }

}
