package pl.gkawalec.pgk.application.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class ProjectHelperTest {

    @Autowired
    private ProjectHelper projectHelper;

    @Autowired
    private TestProjectEntityCreator entityCreator;

    @Autowired
    private TestUserUtil testUserUtil;

    @Test
    @Transactional
    void findProjectManager() {
        //given
        UserEntity userEntity = testUserUtil.insertUser();
        ProjectEntity projectEntity = entityCreator.prepareProjectEntity(userEntity);
        ProjectDTO dto = ProjectDTO.of(projectEntity);

        //when
        UserEntity projectManager = projectHelper.findProjectManager(dto);

        //then
        assertNotNull(projectManager);
        assertEquals(userEntity.getId(), projectManager.getId());
    }

    @Test
    @Transactional
    void findParticipantsByDTO() {
        //given
        List<UserEntity> participants = entityCreator.prepareParticipants();
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithParticipants(participants);
        ProjectDTO dto = ProjectDTO.of(projectEntity);

        //when
        List<UserEntity> foundParticipants = projectHelper.findParticipants(dto);

        //then
        assertNotNull(foundParticipants);
        assertEquals(participants.size(), foundParticipants.size());
        assertArrayEquals(
                participants.stream().map(UserEntity::getId).sorted(Integer::compare).toArray(),
                foundParticipants.stream().map(UserEntity::getId).sorted(Integer::compare).toArray()
        );
    }

    @Test
    @Transactional
    void findParticipantsByIds() {
        //given
        List<UserEntity> participants = entityCreator.prepareParticipants();
        Set<Number> participantIds = participants.stream().map(UserEntity::getId).collect(Collectors.toSet());

        //when
        List<UserEntity> foundParticipants = projectHelper.findParticipants(participantIds);

        //then
        assertNotNull(foundParticipants);
        assertEquals(participants.size(), foundParticipants.size());
        assertArrayEquals(
                participants.stream().map(UserEntity::getId).sorted(Integer::compare).toArray(),
                foundParticipants.stream().map(UserEntity::getId).sorted(Integer::compare).toArray()
        );
    }

    @Test
    @Transactional
    void findLegalActsByDTO() {
        //given
        List<LegalActEntity> legalActs = entityCreator.prepareLegalActs();
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithLegalActs(legalActs);
        ProjectDTO dto = ProjectDTO.of(projectEntity);

        //when
        List<LegalActEntity> foundLegalActs = projectHelper.findLegalActs(dto);

        //then
        assertNotNull(foundLegalActs);
        assertEquals(legalActs.size(), foundLegalActs.size());
        assertArrayEquals(
                legalActs.stream().map(LegalActEntity::getId).sorted(Long::compare).toArray(),
                foundLegalActs.stream().map(LegalActEntity::getId).sorted(Long::compare).toArray()
        );
    }

    @Test
    @Transactional
    void findLegalActsByIds() {
        //given
        List<LegalActEntity> legalActs = entityCreator.prepareLegalActs();
        Set<Number> legalActIds = legalActs.stream().map(LegalActEntity::getId).collect(Collectors.toSet());

        //when
        List<LegalActEntity> foundLegalActs = projectHelper.findLegalActs(legalActIds);

        //then
        assertNotNull(foundLegalActs);
        assertEquals(legalActs.size(), foundLegalActs.size());
        assertArrayEquals(
                legalActs.stream().map(LegalActEntity::getId).sorted(Long::compare).toArray(),
                foundLegalActs.stream().map(LegalActEntity::getId).sorted(Long::compare).toArray()
        );
    }


}
