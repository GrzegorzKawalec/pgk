package pl.gkawalec.pgk.api.dto.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.database.account.user.UserProjectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class ParticipantDTO {

    UserDTO user;
    @Builder.Default
    List<ProjectBaseDTO> projects = new ArrayList<>();

    public static ParticipantDTO of(UserProjectEntity userEntity) {
        UserDTO user = UserDTO.of(userEntity);
        List<ProjectBaseDTO> projects = userEntity.getProjects().stream()
                .map(ProjectBaseDTO::of).collect(Collectors.toList());

        return ParticipantDTO.builder()
                .user(user)
                .projects(projects)
                .build();
    }

}
