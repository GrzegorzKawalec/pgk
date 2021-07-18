package pl.gkawalec.pgk.application.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.api.dto.project.ParticipantDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDataForUpsertDTO;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.UserUtil;
import pl.gkawalec.pgk.database.account.user.BaseUserEntity;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserProjectRepository;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.database.project.ProjectMapper;
import pl.gkawalec.pgk.database.project.ProjectRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectHelper helper;
    private final ProjectUpdater updater;
    private final ProjectValidator validator;

    private final UserProjectRepository userRepository;
    private final ProjectRepository projectRepository;

    public List<ParticipantDTO> getParticipants() {
        return userRepository.findAll().stream()
                .filter(UserUtil::isInternalUser)
                .map(ParticipantDTO::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectDataForUpsertDTO findForUpsertById(Long projectId) {
        ProjectEntity projectEntity = findEntityById(projectId);
        List<UserEntity> participants = projectEntity.getParticipants();
        Set<Number> participantIds = participants.stream().map(BaseUserEntity::getId).collect(Collectors.toSet());
        return ProjectDataForUpsertDTO.of(
                projectEntity,
                userRepository.findAllById(participantIds)
        );
    }

    @Transactional
    public ProjectDTO create(ProjectDTO dto) {
        validator.validateCreate(dto);
        UserEntity projectManager = helper.findProjectManager(dto);
        List<UserEntity> participants = helper.findParticipants(dto);
        List<LegalActEntity> legalActs = helper.findLegalActs(dto);
        ProjectEntity projectEntity = ProjectMapper.create(dto, projectManager, participants, legalActs);
        projectEntity = projectRepository.save(projectEntity);
        return ProjectDTO.of(projectEntity);
    }

    @Transactional
    public ProjectDTO update(ProjectDTO dto) {
        validator.validateUpdate(dto);
        ProjectEntity projectEntity = projectRepository.findOneById(dto.getId());
        updater.update(projectEntity, dto);
        projectEntity = projectRepository.saveAndFlush(projectEntity);
        return ProjectDTO.of(projectEntity);
    }

    private ProjectEntity findEntityById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseException(ResponseExceptionType.PROJECT_NOT_FOUND));
    }

}
