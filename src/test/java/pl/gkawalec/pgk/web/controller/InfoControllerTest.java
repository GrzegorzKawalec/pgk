package pl.gkawalec.pgk.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import pl.gkawalec.pgk.testconfig.annotation.PGKSpringMockMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PGKSpringMockMvcTest
class InfoControllerTest {

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

        //then
        mockMvc
                .perform(get(appSetting.getApiPrefix() + "/info/basic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.version").value(version))
                .andExpect(jsonPath("$.author").value(author));
    }
}
