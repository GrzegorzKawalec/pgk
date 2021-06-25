package pl.gkawalec.pgk.test.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UtilityClass
public class TestRequestPerformerUtil {

    public void performWithoutAuthority(MockMvc mockMvc, MockHttpServletRequestBuilder request) throws Exception {
        mockMvc.perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorUUID", notNullValue()))
                .andExpect(jsonPath("$.type").value(ResponseExceptionType.ACCESS_DENIED.name()))
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.FORBIDDEN.value()));
    }

}
