package pl.gkawalec.pgk.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.testconfig.annotation.PGKSpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PGKSpringBootTest
class ProfileUtilTest {

    @Autowired
    private ProfileUtil profileUtil;

    @Test
    @DisplayName("Test to check if an automatic test profile is set up")
    void isAutomaticTestProfile() {
        // when
        boolean isDevTestProfile = profileUtil.isDevProfile();
        boolean isAutomaticTestProfile = profileUtil.isAutomaticTestProfile();

        // then
        assertFalse(isDevTestProfile, "Profile in automatic tests cannot take the value 'dev'");
        assertTrue(isAutomaticTestProfile, "Return value must be true because profile must be set to automatic tests");
    }
}
