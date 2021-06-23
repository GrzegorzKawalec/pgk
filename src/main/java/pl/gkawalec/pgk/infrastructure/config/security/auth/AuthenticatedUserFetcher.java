package pl.gkawalec.pgk.infrastructure.config.security.auth;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Objects;

@UtilityClass
class AuthenticatedUserFetcher {

    User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isValid(authentication)) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

    private boolean isValid(Authentication authentication) {
        return !authentication.isAuthenticated() ||
                Objects.isNull(authentication.getPrincipal()) ||
                !(authentication.getPrincipal() instanceof User);
    }

}
