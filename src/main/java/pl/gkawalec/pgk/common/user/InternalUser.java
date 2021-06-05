package pl.gkawalec.pgk.common.user;

import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.api.dto.account.UserDTO;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;

abstract class InternalUser implements UserAccessor {

    abstract String getIdentifier();

    @Autowired
    private UserRepository userRepository;

    private UserDTO user;
    private Integer userId;

    @Override
    public UserDTO getUser() {
        if (Objects.isNull(user)) {
            initUser();
        }
        return Objects.nonNull(user) ? user : getDefault();
    }

    @Override
    public Integer getUserId() {
        if (Objects.isNull(userId)) {
            initUserId();
        }
        return Objects.nonNull(userId) ? userId : null;
    }

    @PostConstruct
    private void init() {
        initUser();
    }

    private void initUser() {
        UserEntity userEntity = userRepository.findByEmail(getIdentifier());
        if (Objects.nonNull(userEntity)) {
            user = UserDTO.of(userEntity);
            userId = user.getId();
        }
    }

    private void initUserId() {
        UserEntity userEntity = userRepository.findByEmail(getIdentifier());
        if (Objects.nonNull(userEntity)) {
            userId = userEntity.getId();
        }
    }

    private UserDTO getDefault() {
        return UserDTO.builder()
                .email(getIdentifier())
                .firstName(getIdentifier())
                .lastName(getIdentifier())
                .build();
    }

}
