package pl.gkawalec.pgk.api.dto.account.user;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.user.BaseUserEntity;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class UserDTO {

    Integer id;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    String description;
    @Builder.Default
    boolean isActive = true;
    @Builder.Default
    List<Authority> authorities = new ArrayList<>();

    Integer entityVersion;

    public static UserDTO of(BaseUserEntity entity) {
        return UserDTO.of(entity, null);
    }

    public static UserDTO of(BaseUserEntity entity, List<Authority> authorities) {
        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phoneNumber(entity.getPhoneNumber())
                .description(entity.getDescription())
                .isActive(entity.isActive())
                .entityVersion(entity.getVersion())
                .authorities(authorities)
                .build();
    }

}
