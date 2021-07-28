package pl.gkawalec.pgk.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestControllerUtil;
import pl.gkawalec.pgk.test.util.TestConverterJSONUtil;

import javax.transaction.Transactional;

@PGKSpringMockMvcTest
class ProjectControllerTest {

    private static final String URL = AppSetting.API_PREFIX + ProjectController.URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestControllerUtil testControllerUtil;

    @Test
    @Transactional
    @DisplayName("Test end-point returning project (for upsert) by id. User without the required authorities.")
    void findForUpsertById_withoutAuthorities() throws Exception {
        testControllerUtil.getWithoutAuthorities(
                mockMvc,
                URL + "/" + ProjectController.DATA_FOR_UPSERT + "/" + 1,
                Authority.PROJECT_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point deactivating project. User without the required authorities.")
    void deactivate_withoutAuthorities() throws Exception {
        testControllerUtil.putWithoutAuthorities(
                mockMvc,
                URL + ProjectController.DEACTIVATE + "/" + 1,
                Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point activating project. User without the required authorities.")
    void activate_withoutAuthorities() throws Exception {
        testControllerUtil.putWithoutAuthorities(
                mockMvc,
                URL + ProjectController.ACTIVATE + "/" + 1,
                Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns audited project information. User without the required authorities.")
    void getAuditingInfo_withoutAuthorities() throws Exception {
        testControllerUtil.getWithoutAuthorities(
                mockMvc,
                URL + ProjectController.AUDITING_INFO + "/" + 1,
                Authority.PROJECT_WRITE, Authority.PROJECT_READ, Authority.LEGAL_ACTS_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point creating project. User without the required authorities.")
    void create_withoutAuthorities() throws Exception {
        ProjectEntity entity = prepareProjectEntity();
        ProjectDTO dto = ProjectDTO.of(entity);
        String requestBody = TestConverterJSONUtil.convert(dto);

        testControllerUtil.postWithoutAuthorities(
                mockMvc,
                URL,
                requestBody,
                Authority.PROJECT_WRITE
        );
    }

    @Test
    @Transactional
    @DisplayName("Test end-point updating project. User without the required authorities.")
    void update_withoutAuthorities() throws Exception {
        ProjectEntity entity = prepareProjectEntity();
        ProjectDTO dto = ProjectDTO.of(entity);
        String requestBody = TestConverterJSONUtil.convert(dto);

        testControllerUtil.putWithoutAuthorities(
                mockMvc,
                URL,
                requestBody,
                Authority.PROJECT_WRITE
        );
    }

    private ProjectEntity prepareProjectEntity() {
        ProjectEntity entity = new ProjectEntity();
        ReflectionTestUtils.setField(entity, "id", Long.MAX_VALUE);
        ReflectionTestUtils.setField(entity, ProjectEntity.Fields.projectManager, prepareUserEntity());
        return entity;
    }

    private UserEntity prepareUserEntity() {
        UserEntity entity = new UserEntity();
        ReflectionTestUtils.setField(entity, "id", Integer.MAX_VALUE);
        return entity;
    }

}
