package pl.gkawalec.pgk.api.dto.dashboard;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.database.project.ProjectEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class DashboardDTO {

    @Builder.Default
    List<ProjectDTO> projects = new ArrayList<>();

    public static DashboardDTO of(Collection<ProjectEntity> entities) {
        List<ProjectDTO> projects = entities.stream()
                .map(ProjectDTO::of)
                .collect(Collectors.toList());
        return DashboardDTO.builder()
                .projects(projects)
                .build();
    }

}
