package pl.gkawalec.pgk.infrastructure.config.security.auth;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Objects;

@UtilityClass
class AuthenticatedUserFetcher {

    User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (Objects.isNull(context)) {
            return null;
        }
        Authentication authentication = context.getAuthentication();
        if (isInvalid(authentication)) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

    private boolean isInvalid(Authentication authentication) {
        return Objects.isNull(authentication) ||
                !authentication.isAuthenticated() ||
                Objects.isNull(authentication.getPrincipal()) ||
                !(authentication.getPrincipal() instanceof User);
    }

}
