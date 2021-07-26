package pl.gkawalec.pgk.application.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDataForUpsertDTO;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TestProjectEntityCreator entityCreator;

    @Test
    @DisplayName("Get project that does not exist")
    void findForUpsertById_notExists() {
        //given
        Long projectId = Long.MIN_VALUE;

        //when
        ResponseException ex = assertThrows(ResponseException.class, () -> projectService.findForUpsertById(projectId));

        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getHttpStatus());
        assertEquals(ResponseExceptionType.PROJECT_NOT_FOUND, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Project check does exist")
    void findForUpsertById_exists() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        Long projectId = projectEntity.getId();

        //when
        ProjectDataForUpsertDTO foundProject = projectService.findForUpsertById(projectId);

        //then
        assertNotNull(foundProject);
        assertNotNull(foundProject.getProject());
        assertTrue(CollectionUtil.isNotEmpty(foundProject.getParticipantsProjects()));

        ProjectDTO project = foundProject.getProject();
        compareProject(project, projectEntity);
    }

    @Test
    @Transactional
    @DisplayName("Correct project creation")
    void create_correct() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);

        //when
        ProjectDTO createdProject = projectService.create(dto);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.id, createdProject.getId());

        //then
        compareProject(createdProject, projectEntity);
    }

    @Test
    @Transactional
    @DisplayName("Project was updated correctly")
    void update_correct() {
        //given
        String newName = UUID.randomUUID().toString();
        String newDescription = UUID.randomUUID().toString();
        LocalDate newDateStart = LocalDate.now().minusDays(100);
        LocalDate newDateEnd = LocalDate.now().plusDays(100);
        List<LegalActEntity> newLegalActs = entityCreator.prepareLegalActs();
        List<UserEntity> newParticipants = entityCreator.prepareParticipants();
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        projectEntity.setVersion(projectEntity.getVersion() + 1);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.name, newName);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.description, newDescription);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.dateStart, newDateStart);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.dateEnd, newDateEnd);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.legalActs, newLegalActs);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.participants, newParticipants);
        ReflectionTestUtils.setField(projectEntity, ProjectEntity.Fields.participants, newParticipants);
        ProjectDTO dto = ProjectDTO.of(projectEntity);

        //when
        ProjectDTO updatedProject = projectService.update(dto);

        //then
        compareProject(updatedProject, projectEntity);
    }

    private void compareProject(ProjectDTO dto, ProjectEntity entity) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(entity.getDateStart(), dto.getDateStart());
        assertEquals(entity.getDateEnd(), dto.getDateEnd());
        assertArrayEquals(
                entity.getParticipants().stream().map(UserEntity::getId).sorted(Integer::compareTo).toArray(),
                dto.getParticipants().stream().map(UserDTO::getId).sorted(Integer::compareTo).toArray()
        );
        assertArrayEquals(
                entity.getLegalActs().stream().map(LegalActEntity::getId).sorted(Long::compareTo).toArray(),
                dto.getLegalActs().stream().map(LegalActDTO::getId).sorted(Long::compareTo).toArray()
        );
    }

}
