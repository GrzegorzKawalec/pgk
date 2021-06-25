package pl.gkawalec.pgk.common.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;
import pl.gkawalec.pgk.infrastructure.constant.PGKProfiles;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Primary
@Component
@RequestScope
@RequiredArgsConstructor
public class LoggedUserAccessor implements UserAccessor {

    private final UserRepository userRepository;

    private UserDTO user;
    private Integer userId;

    @Override
    public UserDTO getUser() {
        if (Objects.nonNull(user)) return user;
        user = loadLoggedUserDetails();
        return user;
    }

    @Override
    public Integer getUserId() {
        if (Objects.nonNull(userId)) return userId;
        userId = loadLoggedUserId();
        return userId;
    }

    private UserDTO loadLoggedUserDetails() {
        String email = getLoggedUserEmail();
        if (Objects.isNull(email)) {
            return null;
        }
        UserEntity userEntity = userRepository.findByEmail(email);
        List<Authority> authorities = getAuthorities();
        UserDTO result = UserDTO.of(userEntity, authorities);
        userId = result.getId();
        return result;
    }

    private Integer loadLoggedUserId() {
        if (Objects.nonNull(user)) {
            return user.getId();
        }
        String email = getLoggedUserEmail();
        if (Objects.isNull(email)) {
            return null;
        }
        UserEntity userEntity = userRepository.findByEmail(email);
        return userEntity.getId();
    }

    private List<Authority> getAuthorities() {
        UserDetails userDetails = getLoggedUserDetails();
        if (Objects.isNull(userDetails)) {
            return Collections.emptyList();
        }
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        if (CollectionUtil.isEmpty(authorities)) {
            return Collections.emptyList();
        }
        return authorities.stream()
                .map(a -> Authority.valueOf(a.getAuthority()))
                .collect(Collectors.toList());
    }

    private String getLoggedUserEmail() {
        UserDetails loggedUserDetails = getLoggedUserDetails();
        return Objects.nonNull(loggedUserDetails) ? loggedUserDetails.getUsername() : null;
    }

    private UserDetails getLoggedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || isAnonymous(authentication)) {
            return null;
        }
        return (UserDetails) authentication.getPrincipal();
    }

}
