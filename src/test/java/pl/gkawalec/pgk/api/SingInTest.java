package pl.gkawalec.pgk.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.infrastructure.config.security.PGKWebSecurityConfig;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;
import pl.gkawalec.pgk.test.util.TestLoginUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PGKSpringMockMvcTest
public class SingInTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppSetting appSetting;

    @Autowired
    private TestLoginUtil testLoginUtil;

    @Test
    @DisplayName("Test sing-in with true credentials")
    void signIn_TrueCredentials() throws Exception {
        //given
        String email = appSetting.getEmail().getLogin();
        String pass = appSetting.getEmail().getPassword();

        //when
        MockHttpServletRequestBuilder request = testLoginUtil.buildRequest(email, pass);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test sign-in with universal password")
    void signIn_UniversalPassword() throws Exception {
        //given
        String email = "does not matter";
        String pass = PGKWebSecurityConfig.UNIVERSAL_PASS;

        //when
        MockHttpServletRequestBuilder request = testLoginUtil.buildRequest(email, pass);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test sign-in with wrong password")
    void signIn_WrongPassword() throws Exception {
        //given
        String email = appSetting.getEmail().getLogin();
        String pass = PGKWebSecurityConfig.UNIVERSAL_PASS + PGKWebSecurityConfig.UNIVERSAL_PASS;

        //when
        MockHttpServletRequestBuilder request = testLoginUtil.buildRequest(email, pass);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("Bad credentials"));
    }

    @Test
    @DisplayName("Test sign-in with not existing email address")
    void signIn_NotExistEmail() throws Exception {
        //given
        String email = appSetting.getEmail().getLogin() + appSetting.getEmail().getLogin();
        String pass = appSetting.getEmail().getPassword();

        //when
        MockHttpServletRequestBuilder request = testLoginUtil.buildRequest(email, pass);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("Bad credentials"));
    }

}
