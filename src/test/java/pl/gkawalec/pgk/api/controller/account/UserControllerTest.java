package pl.gkawalec.pgk.api.controller.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.api.dto.account.user.UserUpsertDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestConverterJSONUtil;
import pl.gkawalec.pgk.test.util.TestLoginUtil;
import pl.gkawalec.pgk.test.util.TestRequestPerformerUtil;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import javax.transaction.Transactional;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PGKSpringMockMvcTest
class UserControllerTest {

    private static final String URL = AppSetting.API_PREFIX + UserController.URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppSetting appSetting;

    @Autowired
    private TestUserUtil testUserUtil;

    @Autowired
    private TestLoginUtil testLoginUtil;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test end-point that returns basic information about a logged in user who has not previously logged in")
    void me_Unauthorized() throws Exception {
        //given
        String url = URL + UserController.URL_ME;

        //when
        MockHttpServletRequestBuilder request = get(url);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test end-point that returns basic information about a logged in user, who is admin")
    void me_Admin() throws Exception {
        //given
        String url = URL + UserController.URL_ME;
        String email = appSetting.getEmail().getLogin();
        String pass = appSetting.getEmail().getPassword();
        UserEntity user = userRepository.findByEmail(email);

        //when
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.description").value(user.getDescription()))
                .andExpect(jsonPath("$.authorities").isArray())
                .andExpect(jsonPath("$.authorities", hasItem(Authority.ADMIN.name())));
    }

    @Test
    @Transactional
    @DisplayName("Test end-point creating a user. User without the required authorities.")
    void create_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Set<Authority> correctAuthorities = Set.of(Authority.USER_WRITE, Authority.ROLE_WRITE);
        UserUpsertDTO dto = UserUpsertDTO.builder().build();
        String bodyRequest = TestConverterJSONUtil.convert(dto);

        //when
        testUserUtil.createUserExcludedAuthorities(email, pass, correctAuthorities);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = post(url).session(session)
                .content(bodyRequest).contentType(MediaType.APPLICATION_JSON);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point updating user. User without the required authorities.")
    void update_withoutAuthorities() throws Exception {
        //given
        String url = URL;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Set<Authority> correctAuthorities = Set.of(Authority.USER_WRITE, Authority.ROLE_WRITE);
        UserUpsertDTO dto = UserUpsertDTO.builder().build();
        String bodyRequest = TestConverterJSONUtil.convert(dto);

        //when
        testUserUtil.createUserExcludedAuthorities(email, pass, correctAuthorities);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = put(url, dto).session(session)
                .content(bodyRequest).contentType(MediaType.APPLICATION_JSON);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point deactivating user. User without the required authorities.")
    void getAuditingInfo_withoutAuthorities() throws Exception {
        //given
        String url = URL + UserController.AUDITING_INFO + "/" + 1;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Set<Authority> correctAuthorities = Set.of(Authority.USER_READ, Authority.USER_WRITE);

        //when
        testUserUtil.createUserExcludedAuthorities(email, pass, correctAuthorities);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = get(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

    @Test
    @Transactional
    @DisplayName("Test end-point that returns audited user information. User without the required authorities.")
    void deactivate_withoutAuthorities() throws Exception {
        //given
        String url = URL + UserController.DEACTIVATE + "/" + 1;
        String email = "testowy_a@testowy_a";
        String pass = "password_a";
        Set<Authority> correctAuthorities = Set.of(Authority.USER_WRITE, Authority.ROLE_WRITE);

        //when
        testUserUtil.createUserExcludedAuthorities(email, pass, correctAuthorities);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = put(url).session(session);

        //then
        TestRequestPerformerUtil.performWithoutAuthority(mockMvc, request);
    }

}
