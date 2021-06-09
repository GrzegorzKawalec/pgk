package pl.gkawalec.pgk.application.account.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.api.dto.account.user.UserUpsertDTO;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.database.account.user.*;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    private final UserUniqueEmailChecker uniqueEmailChecker;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    public boolean existsUserEmail(String userEmail, Integer userId) {
        return uniqueEmailChecker.existsTrimEmail(userEmail, userId);
    }

    public UserUpsertDTO findUpsertById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseExceptionType.USER_NOT_FOUND));
        UserCredentialsEntity credentialsEntity = userCredentialsRepository.findByEmail(userEntity.getEmail());
        RoleEntity roleEntity = credentialsEntity.getRole();
        return UserUpsertDTO.of(userEntity, roleEntity);
    }

    @Transactional
    public UserUpsertDTO create(UserUpsertDTO dto) {
        userValidator.validateCreate(dto);
        UserEntity userEntity = UserEntityMapper.create(dto.getUser());
        userEntity = userRepository.save(userEntity);
        RoleEntity roleEntity = roleRepository.findOneById(dto.getRole().getId());
        UserCredentialsEntity credentialsEntity = UserCredentialsEntityMapper.create(userEntity, roleEntity, dto.getPassword());
        userCredentialsRepository.save(credentialsEntity);
        return UserUpsertDTO.of(userEntity, roleEntity);
    }

    @Transactional
    public UserUpsertDTO update(UserUpsertDTO dto) {
        userValidator.validateUpdate(dto);
        UserEntity userEntity = userRepository.findByEmail(dto.getUser().getEmail());
        UserEntityMapper.update(dto.getUser(), userEntity);
        UserCredentialsEntity credentialsEntity = userCredentialsRepository.findByEmail(userEntity.getEmail());
        RoleEntity roleEntity = roleRepository.findOneById(dto.getRole().getId());
        if (UserCredentialsEntityMapper.update(credentialsEntity, roleEntity)) {
            userCredentialsRepository.save(credentialsEntity);
        }
        return UserUpsertDTO.of(userEntity, roleEntity);
    }

}
