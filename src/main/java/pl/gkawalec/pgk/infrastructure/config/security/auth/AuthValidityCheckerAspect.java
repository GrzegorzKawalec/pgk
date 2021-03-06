package pl.gkawalec.pgk.infrastructure.config.security.auth;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.exception.response.auth.AuthorizationInvalidityException;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.user.UserCredentialsEntity;
import pl.gkawalec.pgk.database.account.user.UserCredentialsRepository;
import pl.gkawalec.pgk.infrastructure.constant.AspectOrder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
@Order(AspectOrder.AUTH_VALIDITY_CHECKER)
class AuthValidityCheckerAspect {

    private final UserCredentialsRepository userCredentialsRepository;

    @Pointcut("execution(public * pl.gkawalec.pgk.api.controller..*(..))")
    private void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    private void checkAuthorizationValidity() {
        User user = AuthenticatedUserFetcher.getAuthenticatedUser();
        if (Objects.isNull(user)) {
            return;
        }
        UserCredentialsEntity userCredentialsEntity = userCredentialsRepository.findByEmail(user.getUsername());
        String exceptionMessage = getErrorMessageIfPresent(user, userCredentialsEntity);
        if (Objects.nonNull(exceptionMessage)) {
            SecurityContextHolder.clearContext();
            throw new AuthorizationInvalidityException(exceptionMessage);
        }
    }

    private String getErrorMessageIfPresent(User principal, UserCredentialsEntity userCredentialsEntity) {
        if (!userCredentialsEntity.isActive()) {
            return "Inactive user";
        } else if (hasDifferentAuthorities(principal, userCredentialsEntity)) {
            return "Obsolete authorities";
        }
        return null;
    }

    private boolean hasDifferentAuthorities(User principal, UserCredentialsEntity userCredentialsEntity) {
        Collection<GrantedAuthority> principalAuthorities = principal.getAuthorities();
        List<AuthorityEntity> databaseAuthorities = userCredentialsEntity.getRole().getAuthorities();
        if (principalAuthorities.size() != databaseAuthorities.size()) {
            return true;
        }
        Set<String> principalAuthoritiesSet = principalAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        Set<String> databaseAuthoritiesSet = databaseAuthorities.stream()
                .map(a -> a.getAuthority().name())
                .collect(Collectors.toSet());
        int diffSize = Sets.difference(principalAuthoritiesSet, databaseAuthoritiesSet).size();
        return diffSize > 0;
    }

}
