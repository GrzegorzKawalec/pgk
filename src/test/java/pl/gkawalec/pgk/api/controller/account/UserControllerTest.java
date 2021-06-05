package pl.gkawalec.pgk.api.controller.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestLoginUtil;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

}
