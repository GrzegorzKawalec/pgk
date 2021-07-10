package pl.gkawalec.pgk.application.legalact;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PGKSpringBootTest
class LegalActServiceTest {

    @Autowired
    private LegalActService legalActService;

    @Autowired
    private TestLegalActCreator creator;

    @Test
    @DisplayName("Get legal act that does not exist")
    void findById_notExists() {
        //given
        Long legalActId = Long.MIN_VALUE;

        //when
        ResponseException ex = assertThrows(ResponseException.class, () -> legalActService.findById(legalActId));

        //then
        assertEquals(ex.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(ex.getType(), ResponseExceptionType.LEGAL_ACT_NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("Legal act check does exist")
    void findById_exists() {
        //given
        LegalActEntity legalActEntity = creator.createForNameAndDateOf("name name", LocalDate.now());
        Long id = legalActEntity.getId();

        //when
        LegalActDTO legalActFound = legalActService.findById(id);

        //then
        assertNotNull(legalActFound);
        assertEquals(legalActEntity.getId(), legalActFound.getId());
        assertEquals(legalActEntity.getName(), legalActFound.getName());
        assertEquals(legalActEntity.getDateOf(), legalActFound.getDateOf());
        assertEquals(legalActEntity.getLink(), legalActFound.getLink());
        assertEquals(legalActEntity.getDescription(), legalActFound.getDescription());
        assertEquals(legalActEntity.getVersion(), legalActFound.getEntityVersion());
        assertEquals(1, legalActFound.getEntityVersion(), "This is the first version, so the value must be 1");
    }

    @Test
    @Transactional
    @DisplayName("Correct legal act creation")
    void create_correct() {
        //given
        LegalActDTO dto = TestLegalActDTOPreparer.prepareNew();

        //when
        LegalActDTO createdLegalAct = legalActService.create(dto);

        //then
        assertNotNull(createdLegalAct);
        assertNotNull(createdLegalAct.getId());
        assertEquals(dto.getName(), createdLegalAct.getName());
        assertEquals(dto.getDateOf(), createdLegalAct.getDateOf());
        assertEquals(dto.getLink(), createdLegalAct.getLink());
        assertEquals(dto.getDescription(), createdLegalAct.getDescription());
        assertEquals(1, createdLegalAct.getEntityVersion(), "The version after the update should be incremented");
    }

    @Test
    @Transactional
    @DisplayName("Legal act was updated correctly")
    void update_correct() {
        //given
        String newName = UUID.randomUUID().toString();
        LegalActEntity forEdit = creator.createForLink("www.www.ww");
        LegalActDTO dto = TestLegalActDTOPreparer.prepare(forEdit);
        ReflectionTestUtils.setField(dto, LegalActDTO.Fields.name, newName);

        //when
        LegalActDTO updatedLegalAct = legalActService.update(dto);

        //then
        assertNotNull(updatedLegalAct);
        assertEquals(dto.getId(), updatedLegalAct.getId());
        assertEquals(dto.getName(), updatedLegalAct.getName());
        assertEquals(dto.getDateOf(), updatedLegalAct.getDateOf());
        assertEquals(dto.getLink(), updatedLegalAct.getLink());
        assertEquals(dto.getDescription(), updatedLegalAct.getDescription());
        assertEquals(dto.getEntityVersion() + 1, updatedLegalAct.getEntityVersion(), "The version after the update should be incremented");
    }

}
