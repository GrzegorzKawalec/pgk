package pl.gkawalec.pgk.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.UserDTO;
import pl.gkawalec.pgk.common.annotation.security.AuthGuard;
import pl.gkawalec.pgk.common.exception.response.AccessDeniedResponseException;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.user.LoggedUserAccessor;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthGuardAspect {

    private final LoggedUserAccessor loggedUserAccessor;

    @Before("@annotation(authGuard)")
    private void authGuard(AuthGuard authGuard) {
        if (loggedUserAccessor.isAnonymous()) {
            throw new AccessDeniedResponseException("Anonymous user");
        }
        UserDTO user = loggedUserAccessor.getUser();
        List<Authority> userAuthorities = user.getAuthorities();
        if (userAuthorities.contains(Authority.ADMIN)) {
            return;
        }
        List<Authority> requiredAuthorities = Arrays.asList(authGuard.value());
        if (userAuthorities.stream().noneMatch(requiredAuthorities::contains)) {
            String message = "User without any required authorities. " + getSuffixMessage(user, userAuthorities, requiredAuthorities);
            throw new AccessDeniedResponseException(message);
        }
    }

    private String getSuffixMessage(UserDTO user, List<Authority> authorities, List<Authority> requiredAuthorities) {
        return "UserID: " + user.getId() + "." +
                " Authorities: {" + joinAuthorities(authorities) + "}." +
                " RequiredAuthorities: {" + joinAuthorities(requiredAuthorities) + "}.";
    }

    private String joinAuthorities(List<Authority> authorities) {
        StringJoiner joiner = new StringJoiner(",");
        authorities.forEach(a -> joiner.add(a.name()));
        return joiner.toString();
    }

}
