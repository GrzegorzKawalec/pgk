package pl.gkawalec.pgk.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestControllerUtil;
import pl.gkawalec.pgk.test.util.TestConverterJSONUtil;

import javax.transaction.Transactional;

@PGKSpringMockMvcTest
class LegalActControllerTest {

    private static final String URL = AppSetting.API_PREFIX + LegalActController.URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestControllerUtil testControllerUtil;

    @Test
    @Transactional
    @DisplayName("Test end-point returning legal act by id. User without the required authorities.")
    void findById_withoutAuthorities() throws Exception {
        testControllerUtil.getWithoutAuthorities(
                mockMvc,
                URL + "/" + 1,
                Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns all legal act information. User without the required authorities.")
    void getAll_withoutAuthorities() throws Exception {
        testControllerUtil.getWithoutAuthorities(
                mockMvc,
                URL + LegalActController.ALL,
                Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns audited legal act information. User without the required authorities.")
    void getAuditingInfo_withoutAuthorities() throws Exception {
        testControllerUtil.getWithoutAuthorities(
                mockMvc,
                URL + LegalActController.AUDITING_INFO + "/" + 1,
                Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point deactivating legal act. User without the required authorities.")
    void deactivate_withoutAuthorities() throws Exception {
        testControllerUtil.putWithoutAuthorities(
                mockMvc,
                URL + LegalActController.DEACTIVATE + "/" + 1,
                Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point activating legal act. User without the required authorities.")
    void activate_withoutAuthorities() throws Exception {
        testControllerUtil.putWithoutAuthorities(
                mockMvc,
                URL + LegalActController.ACTIVATE + "/" + 1,
                Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point creating legal act. User without the required authorities.")
    void create_withoutAuthorities() throws Exception {
        LegalActEntity entity = prepareLegalActEntity();
        LegalActDTO dto = LegalActDTO.of(entity);
        String requestBody = TestConverterJSONUtil.convert(dto);

        testControllerUtil.postWithoutAuthorities(
                mockMvc,
                URL,
                requestBody,
                Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point updating legal act. User without the required authorities.")
    void update_withoutAuthorities() throws Exception {
        LegalActEntity entity = prepareLegalActEntity();
        LegalActDTO dto = LegalActDTO.of(entity);
        String requestBody = TestConverterJSONUtil.convert(dto);

        testControllerUtil.putWithoutAuthorities(
                mockMvc,
                URL,
                requestBody,
                Authority.LEGAL_ACTS_WRITE
        );
    }

    private LegalActEntity prepareLegalActEntity() {
        LegalActEntity entity = new LegalActEntity();
        ReflectionTestUtils.setField(entity, "id", Long.MAX_VALUE);
        return entity;
    }

}
