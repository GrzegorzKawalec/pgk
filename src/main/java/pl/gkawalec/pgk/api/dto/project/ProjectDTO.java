package pl.gkawalec.pgk.api.dto.project;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.database.project.ProjectEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@FieldNameConstants
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ProjectDTO extends ProjectBaseDTO {

    private Set<UserDTO> participants = new HashSet<>();
    private UserDTO projectManager;

    private Set<LegalActDTO> legalActs = new HashSet<>();

    private boolean isActive;
    private String description;

    private Integer entityVersion;

    public static ProjectDTO of(ProjectEntity entity) {
        UserDTO projectManager = UserDTO.of(entity.getProjectManager());
        Set<UserDTO> participants = entity.getParticipants().stream().map(UserDTO::of).collect(Collectors.toSet());
        Set<LegalActDTO> legalActs = entity.getLegalActs().stream().map(LegalActDTO::of).collect(Collectors.toSet());

        ProjectDTO result = new ProjectDTO();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setDateStart(entity.getDateStart());
        result.setDateEnd(entity.getDateEnd());
        result.setParticipants(participants);
        result.setProjectManager(projectManager);
        result.setLegalActs(legalActs);
        result.setActive(entity.isActive());
        result.setDescription(entity.getDescription());
        result.setEntityVersion(entity.getVersion());

        return result;
    }

}
