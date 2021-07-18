package pl.gkawalec.pgk.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.project.ProjectEntity;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestConverterJSONUtil;
import pl.gkawalec.pgk.test.util.TestLoginUtil;
import pl.gkawalec.pgk.test.util.TestRequestPerformerUtil;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@PGKSpringMockMvcTest
class ProjectControllerTest {

    private static final String URL = AppSetting.API_PREFIX + ProjectController.URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestLoginUtil testLoginUtil;

    @Autowired
    private TestUserUtil testUserUtil;

    @Test
    @Transactional
    @DisplayName("Test end-point returning project (for upsert) by id. User without the required authorities.")
    void findForUpsertById_withoutAuthorities() throws Exception {
        //given
        String url = URL + "/" + ProjectController.DATA_FOR_UPSERT + "/" + 1;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority correctAuthority = Authority.PROJECT_WRITE;

        //when
        testUserUtil.createUserExcludedAuthority(email, pass, correctAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point creating project. User without the required authorities.")
    void create_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority correctAuthority = Authority.PROJECT_WRITE;
        ProjectEntity entity = prepareProjectEntity();
        ProjectDTO dto = ProjectDTO.of(entity);
        String bodyRequest = TestConverterJSONUtil.convert(dto);

        //when
        testUserUtil.createUserExcludedAuthority(email, pass, correctAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = post(url).session(session)
                .content(bodyRequest).contentType(MediaType.APPLICATION_JSON);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point updating project. User without the required authorities.")
    void update_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority correctAuthority = Authority.PROJECT_WRITE;
        ProjectEntity entity = prepareProjectEntity();
        ProjectDTO dto = ProjectDTO.of(entity);
        String bodyRequest = TestConverterJSONUtil.convert(dto);

        //when
        testUserUtil.createUserExcludedAuthority(email, pass, correctAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = put(url, dto).session(session)
                .content(bodyRequest).contentType(MediaType.APPLICATION_JSON);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
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
