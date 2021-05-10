package pl.gkawalec.pgk.api.dto.user;

import lombok.Builder;
import lombok.Value;
import pl.gkawalec.pgk.common.type.Authority;

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

}
