package pl.gkawalec.pgk.application.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectBaseDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.DateTimeUtil;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class ProjectValidatorTest {

    @Autowired
    private ProjectValidator validator;

    @Autowired
    private TestProjectEntityCreator entityCreator;

    @Test
    @DisplayName("Validate DTO for new project with blank data")
    void validateCreate_emptyData() {
        //given
        ProjectDTO dto = null;

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus(), "Error resulting from data validation, therefore the status should be " + HttpStatus.BAD_REQUEST);
        assertEquals(ResponseExceptionType.EMPTY_DATA, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with blank name")
    void validateCreate_blankName() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntity();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.name, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_NAME, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with blank description")
    void validateCreate_blankDescription() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.description, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_DESCRIPTION, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with blank project manager")
    void validateCreate_blankProjectManager() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.projectManager, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_PROJECT_MANAGER, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with blank participants")
    void validateCreate_blankParticipants() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.participants, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_PARTICIPANTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with blank legal acts")
    void validateCreate_blankLegalActs() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.legalActs, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_LEGAL_ACTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with blank date start")
    void validateCreate_blankDateStart() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStart, null);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStartStr, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_DATE_START, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with blank date end")
    void validateCreate_blankDateEnd() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEnd, null);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEndStr, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_DATE_END, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project with date end before date start")
    void validateCreate_dateEndBeforeDateStart() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        LocalDate dateStart = LocalDate.now().plusDays(1);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStart, dateStart);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStartStr, DateTimeUtil.localDateToString(dateStart));
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEnd, dateEnd);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEndStr, DateTimeUtil.localDateToString(dateEnd));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_DATE_START_IS_BEFORE_END, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Project manager id is null.")
    void validateCreate_projectManagerIdIsNull() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        UserDTO projectManagerDTO = dto.getProjectManager();
        ReflectionTestUtils.setField(projectManagerDTO, UserDTO.Fields.id, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PROJECT_MANAGER, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Not found project manager in database.")
    void validateCreate_notFoundProjectManager() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        UserDTO projectManagerDTO = dto.getProjectManager();
        ReflectionTestUtils.setField(projectManagerDTO, UserDTO.Fields.id, Integer.MIN_VALUE);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PROJECT_MANAGER, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Project manager is not active.")
    void validateCreate_projectManagerIsNotActive() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        entityCreator.deactivateProjectManager(dto);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PROJECT_MANAGER, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Participant id is null.")
    void validateCreate_participantIdIsNull() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getParticipants().forEach(participant -> ReflectionTestUtils.setField(participant, UserDTO.Fields.id, null));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PARTICIPANTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Not found participant in database.")
    void validateCreate_notFoundParticipant() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getParticipants().forEach(participant -> ReflectionTestUtils.setField(participant, UserDTO.Fields.id, Integer.MIN_VALUE));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PARTICIPANTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Participant is not active.")
    void validateCreate_participantIsNotActive() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        entityCreator.deactivateParticipants(dto);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PARTICIPANTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Legal act id is null.")
    void validateCreate_legalActIdIsNull() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getLegalActs().forEach(legalAct -> ReflectionTestUtils.setField(legalAct, LegalActDTO.Fields.id, null));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_LEGAL_ACTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Not found Legal act in database.")
    void validateCreate_notFoundLegalAct() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getLegalActs().forEach(legalAct -> ReflectionTestUtils.setField(legalAct, LegalActDTO.Fields.id, Long.MIN_VALUE));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_LEGAL_ACTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new project. Legal act is not active.")
    void validateCreate_legalActIsNotActive() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        entityCreator.deactivateLegalActs(dto);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_LEGAL_ACTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate correct DTO for new project")
    void validateCreate_correct() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);

        //when
        assertDoesNotThrow(() -> validator.validateCreate(dto));
    }

    @Test
    @DisplayName("Validate DTO for an existing project without data")
    void validateUpdate_emptyData() {
        //given
        ProjectDTO dto = null;

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus(), "Error resulting from data validation, therefore the status should be " + HttpStatus.BAD_REQUEST);
        assertEquals(ResponseExceptionType.EMPTY_DATA, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with blank name")
    void validateUpdate_blankName() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntity();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.name, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_NAME, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with blank description")
    void validateUpdate_blankDescription() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntity();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.description, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_DESCRIPTION, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with blank project manager")
    void validateUpdate_blankProjectManager() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.projectManager, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_PROJECT_MANAGER, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with blank participants")
    void validateUpdate_blankParticipants() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.participants, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_PARTICIPANTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with blank legal acts")
    void validateUpdate_blankLegalActs() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectDTO.Fields.legalActs, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_LEGAL_ACTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with blank date start")
    void validateUpdate_blankDateStart() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStart, null);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStartStr, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_DATE_START, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with blank date end")
    void validateUpdate_blankDateEnd() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEnd, null);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEndStr, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_DATE_END, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with date end before date start")
    void validateUpdate_dateEndBeforeDateStart() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        LocalDate dateStart = LocalDate.now().plusDays(1);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStart, dateStart);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateStartStr, DateTimeUtil.localDateToString(dateStart));
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEnd, dateEnd);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.dateEndStr, DateTimeUtil.localDateToString(dateEnd));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_DATE_START_IS_BEFORE_END, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project without project id")
    void validateUpdate_withoutId() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.id, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_BLANK_ID, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project with project id but not found in database")
    void validateUpdate_notFoundId() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        ReflectionTestUtils.setField(dto, ProjectBaseDTO.Fields.id, Long.MIN_VALUE);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_NOT_FOUND, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project. Project manager id is null.")
    void validateUpdate_projectManagerIdIsNull() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        UserDTO projectManagerDTO = dto.getProjectManager();
        ReflectionTestUtils.setField(projectManagerDTO, UserDTO.Fields.id, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PROJECT_MANAGER, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project. Not found project manager in database.")
    void validateUpdate_notFoundProjectManager() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        UserDTO projectManagerDTO = dto.getProjectManager();
        ReflectionTestUtils.setField(projectManagerDTO, UserDTO.Fields.id, Integer.MIN_VALUE);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PROJECT_MANAGER, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project. Participant id is null.")
    void validateUpdate_participantIdIsNull() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getParticipants().forEach(participant -> ReflectionTestUtils.setField(participant, UserDTO.Fields.id, null));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PARTICIPANTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project. Not found participant in database.")
    void validateUpdate_notFoundParticipant() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getParticipants().forEach(participant -> ReflectionTestUtils.setField(participant, UserDTO.Fields.id, Integer.MIN_VALUE));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_PARTICIPANTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project. Legal act id is null.")
    void validateUpdate_legalActIdIsNull() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getLegalActs().forEach(legalAct -> ReflectionTestUtils.setField(legalAct, LegalActDTO.Fields.id, null));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_LEGAL_ACTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing project. Not found Legal act in database.")
    void validateUpdate_notFoundLegalAct() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        dto.getLegalActs().forEach(legalAct -> ReflectionTestUtils.setField(legalAct, LegalActDTO.Fields.id, Long.MIN_VALUE));

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ResponseExceptionType.PROJECT_INCORRECT_LEGAL_ACTS, ex.getType());
    }

    @Test
    @Transactional
    @DisplayName("Validate correct DTO for an existing project. All data are active: project manager, participants and legal acts.")
    void validateUpdate_correctAllActive() {
        //given
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);

        //when
        assertDoesNotThrow(() -> validator.validateUpdate(dto));
    }

    @Test
    @Transactional
    @DisplayName("Validate correct DTO for an existing project. Some data are inactive: project manager, participants and legal acts.")
    void validateUpdate_correctInActive() {
        ProjectEntity projectEntity = entityCreator.prepareAndSaveProjectEntityWithAllData();
        ProjectDTO dto = ProjectDTO.of(projectEntity);
        entityCreator.deactivateProjectManager(dto);
        entityCreator.deactivateParticipants(dto);
        entityCreator.deactivateLegalActs(dto);

        //when
        assertDoesNotThrow(() -> validator.validateUpdate(dto));
    }

}
