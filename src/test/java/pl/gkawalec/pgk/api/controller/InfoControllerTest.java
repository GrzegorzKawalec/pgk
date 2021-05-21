package pl.gkawalec.pgk.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.test.annotation.PGKSpringMockMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PGKSpringMockMvcTest
class InfoControllerTest {

    private static final String URL = AppSetting.API_PREFIX + InfoController.URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppSetting appSetting;

    @Test
    @DisplayName("Test end-point returning basic info about app. Check returned result with app settings")
    void getBasicInfo() throws Exception {
        //given
        String name = appSetting.getName();
        String version = appSetting.getVersion();
        String author = appSetting.getAuthor();
        String url = URL + InfoController.URL_BASIC;

        //when
        MockHttpServletRequestBuilder request = get(url);

        //then
        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.version").value(version))
                .andExpect(jsonPath("$.author").value(author));
    }
}
