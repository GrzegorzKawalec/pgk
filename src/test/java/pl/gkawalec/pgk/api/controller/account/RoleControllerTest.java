package pl.gkawalec.pgk.api.controller.account;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.api.dto.account.RoleDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.RoleEntity;
import pl.gkawalec.pgk.database.account.RoleRepository;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestConverterJSONUtil;
import pl.gkawalec.pgk.test.util.TestLoginUtil;
import pl.gkawalec.pgk.test.util.TestRequestPerformerUtil;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@PGKSpringMockMvcTest
class RoleControllerTest {

    private static final String URL = AppSetting.API_PREFIX + RoleController.URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestLoginUtil testLoginUtil;

    @Autowired
    private TestUserUtil testUserUtil;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Transactional
    @DisplayName("Test end-point returning roles by id. User without the required authorities.")
    void findById_withoutAuthorities() throws Exception {
        //given
        RoleEntity roleEntity = roleRepository.findAll().stream().findFirst().get();
        String url = URL + "/" + roleEntity.getId();
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority invalidAuthority = Authority.USER_READ;

        //when
        testUserUtil.createUserWithAuthorities(email, pass, invalidAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point creating a role. User without the required authorities.")
    void create_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority invalidAuthority = Authority.USER_READ;
        RoleDTO dto = RoleDTO.builder().name(UUID.randomUUID().toString()).authorities(Set.of(Authority.ROLE_WRITE)).build();
        String bodyRequest = TestConverterJSONUtil.convert(dto);

        //when
        testUserUtil.createUserWithAuthorities(email, pass, invalidAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = post(url).session(session)
                .content(bodyRequest).contentType(MediaType.APPLICATION_JSON);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point updating role. User without the required authorities.")
    void update_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority invalidAuthority = Authority.USER_READ;
        RoleEntity roleEntity = roleRepository.findAll().stream().findFirst().get();
        RoleDTO dto = RoleDTO.of(roleEntity);
        String bodyRequest = TestConverterJSONUtil.convert(dto);

        //when
        testUserUtil.createUserWithAuthorities(email, pass, invalidAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = put(url, dto).session(session)
                .content(bodyRequest).contentType(MediaType.APPLICATION_JSON);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns all authorities. User without the required authorities.")
    void getAllAuthorities_withoutAuthorities() throws Exception {
        //given
        String url = URL + RoleController.AUTHORITIES;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Authority invalidAuthority = Authority.USER_READ;

        //when
        testUserUtil.createUserWithAuthorities(email, pass, invalidAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    private void performRequestWithoutAuthority() {

    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns all authorities. Returned value should contain all available authorities.")
    void getAllAuthorities_ok() throws Exception {
        getAllAuthorities_ok(Authority.ROLE_READ);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns all authorities. Returned value should contain all available authorities. Logged user has ADMIN authority.")
    void getAllAuthorities_okAdmin() throws Exception {
        getAllAuthorities_ok(Authority.ADMIN);
    }

    private void getAllAuthorities_ok(Authority requiredAuthority) throws Exception {
        //given
        String url = URL + RoleController.AUTHORITIES;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Set<Authority> allAuthorities = Set.of(Authority.values());

        //when
        testUserUtil.createUserWithAuthorities(email, pass, requiredAuthority);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(allAuthorities.size())))
                .andExpect(content().string(joinAuthoritiesForJSONCompare(allAuthorities)));
    }

    private String joinAuthoritiesForJSONCompare(Set<Authority> authorities) {
        return "[\"" + Joiner.on("\",\"").join(authorities) + "\"]";
    }

}
