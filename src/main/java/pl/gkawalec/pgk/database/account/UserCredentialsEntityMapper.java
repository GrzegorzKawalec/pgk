package pl.gkawalec.pgk.database.account;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UtilityClass
public class UserCredentialsEntityMapper {

    public UserCredentialsEntity create(UserEntity userEntity, RoleEntity roleEntity, String password) {
        UserCredentialsEntity credentialsEntity = new UserCredentialsEntity();
        credentialsEntity.setEmail(userEntity.getEmail());
        credentialsEntity.setRole(roleEntity);
        credentialsEntity.setPassword(encodePassword(password));
        return credentialsEntity;
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

}
