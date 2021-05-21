package pl.gkawalec.pgk.database.account;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.api.dto.user.UserDTO;

@UtilityClass
public class UserEntityMapper {

    public UserEntity create(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setDescription(dto.getDescription());
        return entity;
    }

}
