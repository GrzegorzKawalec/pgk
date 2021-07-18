package pl.gkawalec.pgk.application.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.legalact.LegalActRepository;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.database.project.ProjectRepository;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
class TestProjectEntityCreator {

    @Autowired
    private TestUserUtil testUserUtil;

    @Autowired
    private LegalActRepository legalActRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    void deactivateLegalActs(ProjectDTO dto) {
        dto.getLegalActs().forEach(legalAct -> {
            LegalActEntity entity = legalActRepository.findOneById(legalAct.getId());
            entity.deactivate();
            legalActRepository.saveAndFlush(entity);
        });
    }

    void deactivateParticipants(ProjectDTO dto) {
        dto.getParticipants().forEach(this::deactivateUser);
    }

    void deactivateProjectManager(ProjectDTO dto) {
        deactivateUser(dto.getProjectManager());
    }

    private void deactivateUser(UserDTO dto) {
        UserEntity entity = userRepository.findOneById(dto.getId());
        entity.deactivate();
        userRepository.saveAndFlush(entity);
    }

    List<UserEntity> prepareParticipants() {
        List<UserEntity> participants = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            participants.add(testUserUtil.insertUser());
        }
        return participants;
    }

    List<LegalActEntity> prepareLegalActs() {
        List<LegalActEntity> legalActs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            legalActs.add(prepareLegalAct());
        }
        return legalActRepository.saveAllAndFlush(legalActs);
    }

    private LegalActEntity prepareLegalAct() {
        LegalActEntity entity = new LegalActEntity();
        ReflectionTestUtils.setField(entity, LegalActEntity.Fields.name, UUID.randomUUID().toString());
        ReflectionTestUtils.setField(entity, LegalActEntity.Fields.dateOf, LocalDate.now());
        ReflectionTestUtils.setField(entity, LegalActEntity.Fields.link, UUID.randomUUID().toString());
        ReflectionTestUtils.setField(entity, LegalActEntity.Fields.isActive, true);
        return entity;
    }

    ProjectEntity prepareAndSaveProjectEntityWithAllData() {
        return projectRepository.saveAndFlush(
                prepareProjectEntityWithAllData()
        );
    }

    ProjectEntity prepareProjectEntityWithAllData() {
        ProjectEntity entity = prepareProjectEntity();
        List<UserEntity> participants = prepareParticipants();
        List<LegalActEntity> legalActs = prepareLegalActs();
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.participants, participants);
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.legalActs, legalActs);
        return entity;
    }

    ProjectEntity prepareProjectEntityWithParticipants(List<UserEntity> participants) {
        ProjectEntity entity = prepareProjectEntity();
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.participants, participants);
        return entity;
    }

    ProjectEntity prepareProjectEntityWithLegalActs(List<LegalActEntity> legalActs) {
        ProjectEntity entity = prepareProjectEntity();
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.legalActs, legalActs);
        return entity;
    }

    ProjectEntity prepareProjectEntity() {
        UserEntity projectManager = testUserUtil.insertUser();
        return prepareProjectEntity(projectManager);
    }

    ProjectEntity prepareProjectEntity(UserEntity projectManager) {
        ProjectEntity entity = new ProjectEntity();
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.id, Long.MAX_VALUE);
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.name, UUID.randomUUID().toString());
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.description, UUID.randomUUID().toString());
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.dateStart, LocalDate.now());
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.dateEnd, LocalDate.now());
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.projectManager, projectManager);
        return entity;
    }

}
