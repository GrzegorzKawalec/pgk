package pl.gkawalec.pgk.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.infrastructure.config.security.PGKWebSecurityConfig;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.testconfig.annotation.PGKSpringMockMvcTest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PGKSpringMockMvcTest
public class SingInTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppSetting appSetting;

    @Test
    @DisplayName("Test sing-in with true credentials")
    void signIn_TrueCredentials() throws Exception {
        //given
        String email = appSetting.getMail().getLogin();
        String pass = appSetting.getMail().getPassword();

        //when
        MockHttpServletRequestBuilder request = buildRequest(email, pass);

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
        MockHttpServletRequestBuilder request = buildRequest(email, pass);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test sign-in with wrong password")
    void signIn_WrongPassword() throws Exception {
        //given
        String email = appSetting.getMail().getLogin();
        String pass = PGKWebSecurityConfig.UNIVERSAL_PASS + PGKWebSecurityConfig.UNIVERSAL_PASS;

        //when
        MockHttpServletRequestBuilder request = buildRequest(email, pass);

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
        String email = appSetting.getMail().getLogin() + appSetting.getMail().getLogin();
        String pass = appSetting.getMail().getPassword();

        //when
        MockHttpServletRequestBuilder request = buildRequest(email, pass);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("Bad credentials"));
    }

    private MockHttpServletRequestBuilder buildRequest(String email, String pass) {
        String emailEncode = URLEncoder.encode(email, StandardCharsets.UTF_8);
        String passEncode = URLEncoder.encode(pass, StandardCharsets.UTF_8);
        String content = "email=" + emailEncode + "&password=" + passEncode;
        return post(PGKWebSecurityConfig.URL_SIGN_IN)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content);
    }

}
