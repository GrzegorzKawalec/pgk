package pl.gkawalec.pgk.api.dto.account.user;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.common.type.Authority;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserSearchEntity;

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
    List<Authority> authorities;

    Integer entityVersion;

    public static UserDTO of(UserEntity entity) {
        return UserDTO.of(entity, null);
    }

    public static UserDTO of(UserEntity entity, List<Authority> authorities) {
        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phoneNumber(entity.getPhoneNumber())
                .description(entity.getDescription())
                .entityVersion(entity.getVersion())
                .authorities(authorities)
                .build();
    }

    public static UserDTO of(UserSearchEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

}
