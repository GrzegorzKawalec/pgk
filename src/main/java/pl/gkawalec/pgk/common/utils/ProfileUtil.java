package pl.gkawalec.pgk.common.utils;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.infrastructure.constant.PGKProfiles;

import java.util.List;

@Component
public class ProfileUtil {

    @Getter
    private final List<String> activeProfiles;

    public ProfileUtil(Environment env) {
        activeProfiles = ImmutableList.copyOf(env.getActiveProfiles());
    }

    public boolean isDevProfile() {
        return isProfile(PGKProfiles.DEV);
    }

    public boolean isAutomaticTestProfile() {
        return isProfile(PGKProfiles.TEST_A);
    }

    private boolean isProfile(String profile) {
        return activeProfiles.stream()
                .anyMatch(profile::equals);
    }

}
