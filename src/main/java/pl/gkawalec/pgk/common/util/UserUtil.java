package pl.gkawalec.pgk.common.util;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.common.user.AnonymousUserAccessor;
import pl.gkawalec.pgk.common.user.SystemUserAccessor;
import pl.gkawalec.pgk.database.account.user.BaseUserEntity;

import java.util.Objects;

@UtilityClass
public class UserUtil {

    public boolean isInternalUser(BaseUserEntity userEntity) {
        if (Objects.isNull(userEntity)) {
            return false;
        }
        String email = userEntity.getEmail();
        if (StringUtil.isBlank(email)) {
            return false;
        }
        return !SystemUserAccessor.IDENTIFIER.equals(email) && !AnonymousUserAccessor.IDENTIFIER.equals(email);
    }

}
