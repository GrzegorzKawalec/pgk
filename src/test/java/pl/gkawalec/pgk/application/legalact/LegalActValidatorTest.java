package pl.gkawalec.pgk.application.legalact;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.DateTimeUtil;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class LegalActValidatorTest {

    @Autowired
    private LegalActValidator validator;

    @Autowired
    private TestLegalActCreator entityCreator;

    @Test
    @DisplayName("Validate DTO for new legal act with blank data")
    void validateCreate_emptyData() {
        //given
        LegalActDTO dto = null;

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST, "Error resulting from data validation, therefore the status should be " + HttpStatus.BAD_REQUEST);
        assertEquals(ex.getType(), ResponseExceptionType.EMPTY_DATA);
    }

    @Test
    @DisplayName("Validate DTO for new legal act with blank name")
    void validateCreate_blankName() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.name, "   ");

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_BLANK_NAME);
    }

    @Test
    @DisplayName("Validate DTO for new legal act with blank date of")
    void validateCreate_blankDateOf() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOf, null);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOfStr, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_BLANK_DATE_OF);
    }

    @Test
    @DisplayName("Validate DTO for new legal act with blank link")
    void validateCreate_blankLink() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.link, "   ");

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_BLANK_LINK);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new legal act with exists link")
    void validateCreate_existsLink() {
        //given
        String link = "www." + UUID.randomUUID() + ".pl";
        entityCreator.createForLink(link);
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.link, link);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_LINK_EXISTS);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for new legal act with exists name (with date of)")
    void validateCreate_existsNameWithDateOf() {
        //given
        String name = UUID.randomUUID().toString();
        LocalDate dateOf = LocalDate.now();
        String dateOfStr = DateTimeUtil.localDateToString(dateOf);
        entityCreator.createForNameAndDateOf(name, dateOf);
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.name, name);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOf, dateOf);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOfStr, dateOfStr);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateCreate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_NAME_WITH_DATE_OF_EXISTS);
    }

    @Test
    @DisplayName("Validate correct DTO for new legal act")
    void validateCreate_correct() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();

        //when
        assertDoesNotThrow(() -> validator.validateCreate(dto));
    }

    @Test
    @DisplayName("Validate DTO for an existing legal act without data")
    void validateUpdate_emptyData() {
        //given
        LegalActDTO dto = null;

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST, "Error resulting from data validation, therefore the status should be " + HttpStatus.BAD_REQUEST);
        assertEquals(ex.getType(), ResponseExceptionType.EMPTY_DATA);
    }

    @Test
    @DisplayName("Validate DTO for an existing legal act with blank name")
    void validateUpdate_blankName() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.name, "   ");

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_BLANK_NAME);
    }

    @Test
    @DisplayName("Validate DTO for an existing legal act with blank date of")
    void validateUpdate_blankDateOf() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOf, null);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOfStr, null);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_BLANK_DATE_OF);
    }

    @Test
    @DisplayName("Validate DTO for an existing legal act with blank link")
    void validateUpdate_blankLink() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.link, "   ");

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_BLANK_LINK);
    }

    @Test
    @DisplayName("Validate DTO for an existing legal act without legal act id")
    void validateUpdate_withoutId() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_BLANK_ID);
    }

    @Test
    @DisplayName("Validate DTO for an existing legal act with legal act id but not found in database")
    void validateUpdate_notFoundId() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.id, Long.MIN_VALUE);

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing legal act with exists link")
    void validateUpdate_existsLink() {
        //given
        String link = "www." + UUID.randomUUID() + ".pl";
        LegalActEntity entityForEdit = entityCreator.createForLink("www.w.www");
        entityCreator.createForLink(link);
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.link, link);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.id, entityForEdit.getId());

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_LINK_EXISTS);
    }

    @Test
    @Transactional
    @DisplayName("Validate DTO for an existing legal act with exists name (with date of)")
    void validateUpdate_existsNameWithDateOf() {
        //given
        String name = UUID.randomUUID().toString();
        LegalActEntity entityForEdit = entityCreator.createForNameAndDateOf("name", LocalDate.now());
        LocalDate dateOf = LocalDate.now();
        String dateOfStr = DateTimeUtil.localDateToString(dateOf);
        entityCreator.createForNameAndDateOf(name, dateOf);
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.name, name);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOf, dateOf);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.dateOfStr, dateOfStr);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.id, entityForEdit.getId());

        //when
        ValidateResponseException ex = assertThrows(ValidateResponseException.class, () -> validator.validateUpdate(dto));

        //then
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_NAME_WITH_DATE_OF_EXISTS);
    }

    @Test
    @Transactional
    @DisplayName("Validate correct DTO for an existing legal act")
    void validateUpdate_correct() {
        //given
        LegalActEntity entityForEdit = entityCreator.createForLink("www.aaa.www");
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.id, entityForEdit.getId());

        //when
        assertDoesNotThrow(() -> validator.validateUpdate(dto));
    }

}
