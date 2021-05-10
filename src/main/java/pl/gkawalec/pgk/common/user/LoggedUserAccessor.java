package pl.gkawalec.pgk.common.user;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.gkawalec.pgk.api.dto.user.UserDTO;

@Component
@RequestScope
public class LoggedUserAccessor implements UserAccessor {

    public UserDTO getUser() {
        return null;
    }

    public Integer getUserId() {
        return null;
    }

}
