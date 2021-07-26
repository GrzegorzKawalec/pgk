package pl.gkawalec.pgk.database.project;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;

import java.util.List;

@UtilityClass
public class ProjectMapper {

    public ProjectEntity create(ProjectDTO dto, UserEntity projectManager, List<UserEntity> participants, List<LegalActEntity> legalActs) {
        ProjectEntity entity = new ProjectEntity();
        changeFlatField(dto, entity);
        updateProjectManager(entity, projectManager);
        updateParticipants(entity, participants);
        updateLegalActs(entity, legalActs);
        entity.setActive(true);
        return entity;
    }

    public void update(ProjectDTO dto, ProjectEntity entity) {
        changeFlatField(dto, entity);
        entity.setVersion(dto.getEntityVersion());
    }

    public void updateProjectManager(ProjectEntity entity, UserEntity projectManager) {
        entity.setProjectManager(projectManager);
    }

    public void updateParticipants(ProjectEntity entity, List<UserEntity> participants) {
        entity.setParticipants(participants);
    }

    public void updateLegalActs(ProjectEntity entity, List<LegalActEntity> legalActs) {
        entity.setLegalActs(legalActs);
    }

    private void changeFlatField(ProjectDTO dto, ProjectEntity entity) {
        entity.setName(dto.getName());
        entity.setDateStart(dto.getDateStart());
        entity.setDateEnd(dto.getDateEnd());
        entity.setDescription(dto.getDescription());
    }

}
