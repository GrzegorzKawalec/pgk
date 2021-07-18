package pl.gkawalec.pgk.application.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.account.user.UserDTO;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.api.dto.project.ProjectDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.account.user.UserRepository;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.database.legalact.LegalActRepository;
import pl.gkawalec.pgk.database.project.ProjectRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
class ProjectValidator {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final LegalActRepository legalActRepository;

    void validateCreate(ProjectDTO dto) {
        validateFieldDTO(dto);
        validateInDatabase(dto, true);
    }

    void validateUpdate(ProjectDTO dto) {
        validateFieldDTO(dto);
        if (Objects.isNull(dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_ID);
        }
        if (!projectRepository.existsById(dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_NOT_FOUND);
        }
        validateInDatabase(dto, false);
    }

    private void validateFieldDTO(ProjectDTO dto) {
        if (Objects.isNull(dto)) {
            throw new ValidateResponseException(ResponseExceptionType.EMPTY_DATA);
        }
        if (StringUtil.isBlank(dto.getName())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_NAME);
        }
        if (StringUtil.isBlank(dto.getDescription())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_DESCRIPTION);
        }
        if (Objects.isNull(dto.getProjectManager())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_PROJECT_MANAGER);
        }
        if (CollectionUtil.isEmpty(dto.getParticipants())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_PARTICIPANTS);
        }
        if (CollectionUtil.isEmpty(dto.getLegalActs())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_LEGAL_ACTS);
        }
        validateDate(dto);
    }

    private void validateDate(ProjectDTO dto) {
        if (Objects.isNull(dto.getDateStart())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_DATE_START);
        }
        if (Objects.isNull(dto.getDateEnd())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_BLANK_DATE_END);
        }
        if (dto.getDateEnd().isBefore(dto.getDateStart())) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_DATE_START_IS_BEFORE_END);
        }
    }

    private void validateInDatabase(ProjectDTO dto, boolean mustBeActive) {
        if (isNotValidUser(dto.getProjectManager(), mustBeActive)) {
            throw new ValidateResponseException(ResponseExceptionType.PROJECT_INCORRECT_PROJECT_MANAGER);
        }
        dto.getParticipants().forEach(participant -> {
            if (isNotValidUser(participant, mustBeActive)) {
                throw new ValidateResponseException(ResponseExceptionType.PROJECT_INCORRECT_PARTICIPANTS);
            }
        });
        dto.getLegalActs().forEach(legalAct -> {
            if (isNotValidLegalAct(legalAct, mustBeActive)) {
                throw new ValidateResponseException(ResponseExceptionType.PROJECT_INCORRECT_LEGAL_ACTS);
            }
        });
    }

    private boolean isNotValidUser(UserDTO dto, boolean mustBeActive) {
        UserEntity userEntity = Objects.isNull(dto.getId()) ? null :
                userRepository.findOneById(dto.getId());
        if (Objects.isNull(userEntity) || (mustBeActive && !userEntity.isActive())) {
            return true;
        }
        return false;
    }

    private boolean isNotValidLegalAct(LegalActDTO dto, boolean mustBeActive) {
        LegalActEntity legalActEntity = Objects.isNull(dto.getId()) ? null :
                legalActRepository.findOneById(dto.getId());
        if (Objects.isNull(legalActEntity) || (mustBeActive && !legalActEntity.isActive())) {
            return true;
        }
        return false;
    }

}
