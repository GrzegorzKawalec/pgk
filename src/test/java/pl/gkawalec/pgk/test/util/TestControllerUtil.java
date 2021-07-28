package pl.gkawalec.pgk.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.test.annotation.PGKTestProfiles;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@PGKTestProfiles
public class TestControllerUtil {

    @Autowired
    private TestLoginUtil testLoginUtil;

    @Autowired
    private TestUserUtil testUserUtil;

    public void getWithoutAuthorities(MockMvc mockMvc, String url, Authority... correctAuthorities) throws Exception {
        testWithoutAuthorities(mockMvc, url, correctAuthorities, MockMvcRequestBuilders::get);
    }

    public void putWithoutAuthorities(MockMvc mockMvc, String url, Authority... correctAuthorities) throws Exception {
        testWithoutAuthorities(mockMvc, url, correctAuthorities, MockMvcRequestBuilders::put);
    }

    public void putWithoutAuthorities(MockMvc mockMvc, String url, String requestBody, Authority... correctAuthorities) throws Exception {
        testWithoutAuthorities(mockMvc, url, correctAuthorities, MockMvcRequestBuilders::put, requestBody);
    }

    public void postWithoutAuthorities(MockMvc mockMvc, String url, String requestBody, Authority... correctAuthorities) throws Exception {
        testWithoutAuthorities(mockMvc, url, correctAuthorities, MockMvcRequestBuilders::post, requestBody);
    }

    public void deleteWithoutAuthorities(MockMvc mockMvc, String url, Authority... correctAuthorities) throws Exception {
        testWithoutAuthorities(mockMvc, url, correctAuthorities, MockMvcRequestBuilders::delete);
    }

    private void testWithoutAuthorities(MockMvc mockMvc, String url, Authority[] correctAuthoritiesArray,
                                       Function<String, MockHttpServletRequestBuilder> requestMethod) throws Exception {
        testWithoutAuthorities(mockMvc, url, correctAuthoritiesArray, requestMethod, null);
    }

    private void testWithoutAuthorities(MockMvc mockMvc, String url, Authority[] correctAuthoritiesArray,
                                       Function<String, MockHttpServletRequestBuilder> requestMethod, String requestBody) throws Exception {
        //given
        String email = UUID.randomUUID() + "@pgk.pl";
        String pass = UUID.randomUUID().toString();
        Set<Authority> correctAuthorities = Set.of(correctAuthoritiesArray);

        //when
        testUserUtil.createUserExcludedAuthorities(email, pass, correctAuthorities);
        MockHttpSession session = testLoginUtil.loginSession(mockMvc, email, pass);
        MockHttpServletRequestBuilder request = requestMethod.apply(url).session(session);
        if (StringUtil.isNotBlank(requestBody)) {
            request = request.content(requestBody).contentType(MediaType.APPLICATION_JSON);
        }

        //then
        performWithoutAuthority(mockMvc, request);
    }

    private void performWithoutAuthority(MockMvc mockMvc, MockHttpServletRequestBuilder request) throws Exception {
        mockMvc.perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorUUID", notNullValue()))
                .andExpect(jsonPath("$.type").value(ResponseExceptionType.ACCESS_DENIED.name()))
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.FORBIDDEN.value()));
    }

}
