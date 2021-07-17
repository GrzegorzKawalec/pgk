package pl.gkawalec.pgk.database.account.user;

import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;

@UtilityClass
public class UserEntityMapper {

    public UserEntity create(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setDescription(dto.getDescription());
        entity.activate();
        return entity;
    }

    public void update(UserDTO dto, UserEntity entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setDescription(dto.getDescription());
        entity.setVersion(dto.getEntityVersion());
    }

}
