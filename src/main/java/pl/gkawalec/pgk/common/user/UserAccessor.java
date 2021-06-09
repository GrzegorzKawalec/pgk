package pl.gkawalec.pgk.common.user;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;

public interface UserAccessor {

    UserDTO getUser();
    Integer getUserId();

    default boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAnonymous(authentication);
    }

    default boolean isAnonymous(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken;
    }

}
