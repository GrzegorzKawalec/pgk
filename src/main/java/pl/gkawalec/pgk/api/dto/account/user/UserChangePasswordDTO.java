package pl.gkawalec.pgk.api.dto.account.user;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.common.annotation.request.NotAuditedRequestType;

@Value
@Builder
public class UserChangePasswordDTO {

    Integer userId;

    @NotAuditedRequestType
    String password;

}
