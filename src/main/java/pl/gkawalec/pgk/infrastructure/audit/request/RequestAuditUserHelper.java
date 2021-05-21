package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.user.AnonymousUserAccessor;
import pl.gkawalec.pgk.common.user.LoggedUserAccessor;

@Component
@RequiredArgsConstructor
class RequestAuditUserHelper {

    private final LoggedUserAccessor loggedUserAccessor;
    private final AnonymousUserAccessor anonymousUserAccessor;

    Integer getUserId() {
        return anonymousUserAccessor.isAnonymous() ?
                anonymousUserAccessor.getUserId() :
                loggedUserAccessor.getUserId();
    }

}
