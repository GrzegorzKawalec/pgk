package pl.gkawalec.pgk.database.account.user;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.gkawalec.pgk.database.account.role.RoleEntity;

@UtilityClass
public class UserCredentialsEntityMapper {

    public UserCredentialsEntity create(UserEntity userEntity, RoleEntity roleEntity, String password) {
        UserCredentialsEntity credentialsEntity = new UserCredentialsEntity();
        credentialsEntity.setEmail(userEntity.getEmail());
        credentialsEntity.setRole(roleEntity);
        credentialsEntity.setPassword(encodePassword(password));
        credentialsEntity.setActive(true);
        return credentialsEntity;
    }

    public boolean update(UserCredentialsEntity credentialsEntity, RoleEntity roleEntity) {
        RoleEntity oldRoleEntity = credentialsEntity.getRole();
        if (oldRoleEntity.getId().equals(roleEntity.getId())) {
            return false;
        }
        credentialsEntity.setRole(roleEntity);
        return true;
    }

    public void changePassword(UserCredentialsEntity credentialsEntity, String password) {
        credentialsEntity.setPassword(encodePassword(password));
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

}
