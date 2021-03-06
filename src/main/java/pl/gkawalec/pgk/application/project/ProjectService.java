package pl.gkawalec.pgk.application.project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;
import pl.gkawalec.pgk.api.dto.project.*;
import pl.gkawalec.pgk.common.auditing.AuditingMapper;
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
import pl.gkawalec.pgk.database.project.ProjectSpecification;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectHelper helper;
    private final ProjectUpdater updater;
    private final ProjectValidator validator;

    private final AuditingMapper auditingMapper;

    private final UserProjectRepository userRepository;
    private final ProjectRepository projectRepository;

    public List<ParticipantDTO> getParticipants() {
        return userRepository.findAll().stream()
                .filter(UserUtil::isInternalUser)
                .map(ParticipantDTO::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<ProjectDTO> find(ProjectCriteria criteria) {
        criteria = Objects.nonNull(criteria) ? criteria : new ProjectCriteria();
        ProjectSpecification specification = new ProjectSpecification(criteria);
        PageRequest pageRequest = criteria.getSearchPage().toPageRequest();
        return projectRepository.findAll(specification, pageRequest)
                .map(ProjectDTO::of);
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

    @Transactional
    public void deactivate(Long projectId) {
        ProjectEntity projectEntity = findEntityById(projectId);
        projectEntity.deactivate();
    }

    @Transactional
    public void activate(Long projectId) {
        ProjectEntity projectEntity = findEntityById(projectId);
        projectEntity.activate();
    }

    @Transactional
    public ProjectAuditingDTO getAuditingInfo(Long projectId) {
        ProjectEntity projectEntity = findEntityById(projectId);
        AuditingInfoDTO auditingInfoDTO = auditingMapper.map(projectEntity);
        return ProjectAuditingDTO.of(projectEntity, auditingInfoDTO);
    }

    private ProjectEntity findEntityById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseException(ResponseExceptionType.PROJECT_NOT_FOUND));
    }

}
