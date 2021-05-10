package pl.gkawalec.pgk.common.user;

import pl.gkawalec.pgk.api.dto.user.UserDTO;

abstract class InternalUser implements UserAccessor {

    abstract String getIdentifier();

    @Override
    public UserDTO getUser() {
        return null;
    }

    @Override
    public Integer getUserId() {
        return null;
    }

}
