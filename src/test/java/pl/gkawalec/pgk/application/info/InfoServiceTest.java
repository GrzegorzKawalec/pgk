package pl.gkawalec.pgk.application.info;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.testconfig.annotation.PGKSpringBootTest;
import pl.gkawalec.pgk.api.dto.info.InfoBasicDTO;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PGKSpringBootTest
class InfoServiceTest {

    @Autowired
    private InfoService infoService;

    @Test
    @DisplayName("Test a service method that returns basic info about app. Check for non-empty data.")
    void getBasicInfo() {
        // when
        InfoBasicDTO basicInfo = infoService.getBasicInfo();

        // then
        assertNotNull(basicInfo, "Return value of basic info cannot be null");
        assertTrue(StringUtils.isNotBlank(basicInfo.getName()), "App name in basic info is required");
        assertTrue(StringUtils.isNotBlank(basicInfo.getVersion()), "App version in basic info is required");
    }
}
