package pl.gkawalec.pgk.infrastructure.config.security;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.database.account.user.UserCredentialsEntity;
import pl.gkawalec.pgk.database.account.user.UserCredentialsRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Order(0)
@Component
@RequiredArgsConstructor
class RequestAuthorizationValidityChecker {

    private final UserCredentialsRepository userCredentialsRepository;

    @Pointcut("execution(public * pl.gkawalec.pgk.api.controller..*(..))")
    private void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    private void requestValidityChecker() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (cannotCheck(authentication)) {
            return;
        }
        User principal = (User) authentication.getPrincipal();
        UserCredentialsEntity userCredentialsEntity = userCredentialsRepository.findByEmail(principal.getUsername());
        if (!userCredentialsEntity.isActive() || hasDifferentAuthorities(principal, userCredentialsEntity)) {
            SecurityContextHolder.clearContext();
        }
    }

    private boolean cannotCheck(Authentication authentication) {
        return !authentication.isAuthenticated() ||
                Objects.isNull(authentication.getPrincipal()) ||
                !(authentication.getPrincipal() instanceof User);
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
