package pl.gkawalec.pgk.api.dto.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingDTO;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;
import pl.gkawalec.pgk.database.project.ProjectEntity;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class ProjectAuditingDTO implements AuditingDTO<ProjectDTO> {

    ProjectDTO dto;
    AuditingInfoDTO info;

    public static ProjectAuditingDTO of(ProjectEntity entity, AuditingInfoDTO auditing) {
        ProjectDTO project = ProjectDTO.of(entity);
        return ProjectAuditingDTO.builder()
                .dto(project)
                .info(auditing)
                .build();
    }

}
