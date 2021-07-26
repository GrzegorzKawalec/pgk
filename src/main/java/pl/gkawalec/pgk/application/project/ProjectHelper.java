package pl.gkawalec.pgk.application.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.legalact.LegalActRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class ProjectHelper {

    private final UserRepository userRepository;
    private final LegalActRepository legalActRepository;

    UserEntity findProjectManager(ProjectDTO dto) {
        UserDTO projectManager = dto.getProjectManager();
        return userRepository.findOneById(projectManager.getId());
    }

    List<UserEntity> findParticipants(ProjectDTO dto) {
        Set<UserDTO> participants = dto.getParticipants();
        Set<Number> participantIds = participants.stream().map(UserDTO::getId).collect(Collectors.toSet());
        return findParticipants(participantIds);
    }

    List<UserEntity> findParticipants(Set<Number> participantIds) {
        return userRepository.findAllById(participantIds);
    }

    List<LegalActEntity> findLegalActs(ProjectDTO dto) {
        Set<LegalActDTO> legalActs = dto.getLegalActs();
        Set<Number> legalActIds = legalActs.stream().map(LegalActDTO::getId).collect(Collectors.toSet());
        return findLegalActs(legalActIds);
    }

    List<LegalActEntity> findLegalActs(Set<Number> legalActIds) {
        return legalActRepository.findAllById(legalActIds);
    }

}
