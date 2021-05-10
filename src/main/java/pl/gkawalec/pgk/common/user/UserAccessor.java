package pl.gkawalec.pgk.common.user;

import pl.gkawalec.pgk.api.dto.user.UserDTO;

public interface UserAccessor {

    UserDTO getUser();
    Integer getUserId();

}
