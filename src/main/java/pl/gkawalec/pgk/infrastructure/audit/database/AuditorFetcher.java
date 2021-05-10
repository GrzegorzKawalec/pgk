package pl.gkawalec.pgk.infrastructure.audit.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import pl.gkawalec.pgk.common.user.AnonymousUserAccessor;
import pl.gkawalec.pgk.common.user.LoggedUserAccessor;
import pl.gkawalec.pgk.common.user.SystemUserAccessor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditorFetcher {

    private final LoggedUserAccessor userAccessor;
    private final SystemUserAccessor systemUserAccessor;
    private final AnonymousUserAccessor anonymousUserAccessor;

    public Optional<Auditor> getAuditor() {
        Integer userId = getUserId();
        LocalDateTime now = LocalDateTime.now();
        return Optional.of(new Auditor(userId, now));
    }

    private Integer getUserId() {
        if (isRequest()) {
            Integer loggedUserId = userAccessor.getUserId();
            return Objects.nonNull(loggedUserId) ? loggedUserId :
                    anonymousUserAccessor.getUserId();
        } else {
            return systemUserAccessor.getUserId();
        }
    }

    private boolean isRequest() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }

}
