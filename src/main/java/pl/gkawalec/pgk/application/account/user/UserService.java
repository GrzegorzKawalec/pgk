package pl.gkawalec.pgk.application.account.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.gkawalec.pgk.api.dto.account.user.*;
import pl.gkawalec.pgk.api.dto.common.SelectDTO;
import pl.gkawalec.pgk.api.dto.common.auditing.AuditingInfoDTO;
import pl.gkawalec.pgk.common.auditing.AuditingMapper;
import pl.gkawalec.pgk.common.exception.response.ResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.UserUtil;
import pl.gkawalec.pgk.database.account.role.RoleEntity;
import pl.gkawalec.pgk.database.account.role.RoleRepository;
import pl.gkawalec.pgk.database.account.user.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    private final UserUniqueEmailChecker uniqueEmailChecker;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    private final AuditingMapper auditingMapper;

    public boolean existsUserEmail(String userEmail, Integer excludedUserId) {
        return uniqueEmailChecker.existsTrimEmail(userEmail, excludedUserId);
    }

    public UserUpsertDTO findUpsertById(Integer userId) {
        UserEntity userEntity = findUserEntityById(userId);
        return prepareUserUpsertDTO(userEntity);
    }

    public List<SelectDTO> getSelectedUsers() {
        return userRepository.findAllByIsActiveTrue().stream()
                .filter(UserUtil::isInternalUser)
                .map(SelectDTO::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivate(Integer userId) {
        userValidator.validateDeactivate(userId);
        UserEntity userEntity = userRepository.findOneById(userId);
        UserCredentialsEntity credentialsEntity = userCredentialsRepository.findByEmail(userEntity.getEmail());
        userEntity.deactivate();
        credentialsEntity.deactivate();
    }

    @Transactional
    public void activate(Integer userId) {
        UserEntity userEntity = findUserEntityById(userId);
        UserCredentialsEntity credentialsEntity = userCredentialsRepository.findByEmail(userEntity.getEmail());
        userEntity.activate();
        credentialsEntity.activate();
    }

    @Transactional
    public void changePassword(UserChangePasswordDTO dto) {
        userValidator.validateChangePassword(dto);
        UserEntity userEntity = userRepository.findOneById(dto.getUserId());
        UserCredentialsEntity userCredentialsEntity = userCredentialsRepository.findByEmail(userEntity.getEmail());
        UserCredentialsEntityMapper.changePassword(userCredentialsEntity, dto.getPassword());
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

    public Page<UserSearchDTO> find(UserCriteria criteria) {
        criteria = Objects.nonNull(criteria) ? criteria : new UserCriteria();
        UserSpecification specification = new UserSpecification(criteria);
        PageRequest pageRequest = criteria.getSearchPage().toPageRequest();
        return userSearchRepository.findAll(specification, pageRequest)
                .map(UserSearchDTO::of);
    }

    public UserAuditingDTO getAuditingInfo(Integer userId) {
        UserEntity userEntity = findUserEntityById(userId);
        UserUpsertDTO userDTO = prepareUserUpsertDTO(userEntity);
        AuditingInfoDTO auditingInfoDTO = auditingMapper.map(userEntity);
        return UserAuditingDTO.builder()
                .dto(userDTO)
                .info(auditingInfoDTO)
                .build();
    }

    private UserUpsertDTO prepareUserUpsertDTO(UserEntity userEntity) {
        UserCredentialsEntity credentialsEntity = userCredentialsRepository.findByEmail(userEntity.getEmail());
        RoleEntity roleEntity = credentialsEntity.getRole();
        return UserUpsertDTO.of(userEntity, roleEntity);
    }

    private UserEntity findUserEntityById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseExceptionType.USER_NOT_FOUND));
    }

}
