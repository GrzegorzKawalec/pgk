package pl.gkawalec.pgk.api.dto.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.common.util.DateTimeUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class SelectDTO {

    Number id;
    String value;
    String additionalInfo;

    public static SelectDTO of(LegalActEntity entity) {
        return SelectDTO.builder()
                .id(entity.getId())
                .value(entity.getName())
                .additionalInfo(DateTimeUtil.localDateToString(entity.getDateOf()))
                .build();
    }

    public static SelectDTO of(UserEntity entity) {
        return SelectDTO.builder()
                .id(entity.getId())
                .value(entity.getLastName() + " " + entity.getFirstName())
                .additionalInfo(entity.getEmail())
                .build();
    }

}
