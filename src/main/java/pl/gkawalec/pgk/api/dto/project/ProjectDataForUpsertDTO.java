package pl.gkawalec.pgk.api.dto.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.database.account.user.UserProjectEntity;
import pl.gkawalec.pgk.database.project.ProjectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class ProjectDataForUpsertDTO {

    ProjectDTO project;
    @Builder.Default
    List<ProjectBaseDTO> participantsProjects = new ArrayList<>();

    public static ProjectDataForUpsertDTO of(ProjectEntity projectEntity, List<UserProjectEntity> usersProjectsEntities) {
        ProjectDTO projectDTO = ProjectDTO.of(projectEntity);
        List<ProjectBaseDTO> participantsProjectsDTO = usersProjectsEntities.stream()
                .map(UserProjectEntity::getProjects)
                .flatMap(List::stream)
                .map(ProjectBaseDTO::of)
                .collect(Collectors.toList());
        return ProjectDataForUpsertDTO.builder()
                .project(projectDTO)
                .participantsProjects(participantsProjectsDTO)
                .build();
    }

}
