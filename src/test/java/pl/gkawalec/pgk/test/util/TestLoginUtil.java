package pl.gkawalec.pgk.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.infrastructure.config.security.PGKWebSecurityConfig;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKTestProfiles;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
@PGKTestProfiles
public class TestLoginUtil {

    private static final String LOGIN_SESSION_ATTR_NAME = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private AppSetting appSetting;

    public MockHttpSession loginSessionForAdmin(MockMvc mockMvc) throws Exception {
        String email = appSetting.getEmail().getLogin();
        String pass = appSetting.getEmail().getPassword();
        return loginSession(mockMvc, email, pass);
    }

    public MockHttpSession loginSession(MockMvc mockMvc, String email, String pass) throws Exception {
        MockHttpServletRequestBuilder request = buildRequest(email, pass);

        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();

        assert Objects.nonNull(session);
        Object sessionAttr = session.getAttribute(LOGIN_SESSION_ATTR_NAME);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(LOGIN_SESSION_ATTR_NAME, sessionAttr);
        return mockSession;
    }

    public MockHttpServletRequestBuilder buildRequest(String email, String pass) {
        String emailEncode = URLEncoder.encode(email, StandardCharsets.UTF_8);
        String passEncode = URLEncoder.encode(pass, StandardCharsets.UTF_8);
        String content = "email=" + emailEncode + "&password=" + passEncode;
        return post(PGKWebSecurityConfig.URL_SIGN_IN)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content);
    }

}
