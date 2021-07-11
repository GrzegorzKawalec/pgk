package pl.gkawalec.pgk.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestConverterJSONUtil;
import pl.gkawalec.pgk.test.util.TestLoginUtil;
import pl.gkawalec.pgk.test.util.TestRequestPerformerUtil;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import javax.transaction.Transactional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@PGKSpringMockMvcTest
class LegalActControllerTest {

    private static final String URL = AppSetting.API_PREFIX + LegalActController.URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestLoginUtil testLoginUtil;

    @Autowired
    private TestUserUtil testUserUtil;

    @Test
    @Transactional
    @DisplayName("Test end-point returning legal act by id. User without the required authorities.")
    void findById_withoutAuthorities() throws Exception {
        //given
        String url = URL + "/" + 1;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Set<Authority> correctAuthorities = Set.of(Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE);

        //when
        testUserUtil.createUserExcludedAuthorities(email, pass, correctAuthorities);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns audited legal act information. User without the required authorities.")
    void getAuditingInfo_withoutAuthorities() throws Exception {
        //given
        String url = URL + LegalActController.AUDITING_INFO + "/" + 1;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Set<Authority> correctAuthorities = Set.of(Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE);

        //when
        testUserUtil.createUserExcludedAuthorities(email, pass, correctAuthorities);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point deactivating legal act. User without the required authorities.")
    void deactivate_withoutAuthorities() throws Exception {
        //given
        String url = URL + LegalActController.DEACTIVATE + "/" + 1;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority correctAuthority = Authority.LEGAL_ACTS_WRITE;

        //when
        testUserUtil.createUserExcludedAuthority(email, pass, correctAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = put(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point activating legal act. User without the required authorities.")
    void activate_withoutAuthorities() throws Exception {
        //given
        String url = URL + LegalActController.ACTIVATE + "/" + 1;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority correctAuthority = Authority.LEGAL_ACTS_WRITE;

        //when
        testUserUtil.createUserExcludedAuthority(email, pass, correctAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = put(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point creating legal act. User without the required authorities.")
    void create_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority correctAuthority = Authority.LEGAL_ACTS_WRITE;
        LegalActEntity entity = prepareLegalActEntity();
        LegalActDTO dto = LegalActDTO.of(entity);
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
    @DisplayName("Test end-point updating legal act. User without the required authorities.")
    void update_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority correctAuthority = Authority.LEGAL_ACTS_WRITE;
        LegalActEntity entity = prepareLegalActEntity();
        LegalActDTO dto = LegalActDTO.of(entity);
        String bodyRequest = TestConverterJSONUtil.convert(dto);

        //when
        testUserUtil.createUserExcludedAuthority(email, pass, correctAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = put(url, dto).session(session)
                .content(bodyRequest).contentType(MediaType.APPLICATION_JSON);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    private LegalActEntity prepareLegalActEntity() {
        LegalActEntity entity = new LegalActEntity();
        ReflectionTestUtils.setField(entity, "id", Long.MAX_VALUE);
        return entity;
    }

}
