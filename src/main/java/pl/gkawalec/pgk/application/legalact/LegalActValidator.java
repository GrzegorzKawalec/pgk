package pl.gkawalec.pgk.application.legalact;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.api.dto.legalact.LegalActDTO;
import pl.gkawalec.pgk.common.exception.response.ValidateResponseException;
import pl.gkawalec.pgk.common.type.ResponseExceptionType;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.legalact.LegalActRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
class LegalActValidator {

    private final LegalActUniqueChecker uniqueChecker;
    private final LegalActRepository legalActRepository;

    void validateCreate(LegalActDTO dto) {
        validateFieldDTO(dto);
        checkUnique(dto);
    }

    void validateUpdate(LegalActDTO dto) {
        validateFieldDTO(dto);
        if (Objects.isNull(dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.LEGAL_ACT_BLANK_ID);
        }
        if (!legalActRepository.existsById(dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.LEGAL_ACT_NOT_FOUND);
        }
        checkUnique(dto);
    }

    private void validateFieldDTO(LegalActDTO dto) {
        if (Objects.isNull(dto)) {
            throw new ValidateResponseException(ResponseExceptionType.EMPTY_DATA);
        }
        if (StringUtil.isBlank(dto.getName())) {
            throw new ValidateResponseException(ResponseExceptionType.LEGAL_ACT_BLANK_NAME);
        }
        if (Objects.isNull(dto.getDateOf())) {
            throw new ValidateResponseException(ResponseExceptionType.LEGAL_ACT_BLANK_DATE_OF);
        }
        if (StringUtil.isBlank(dto.getLink())) {
            throw new ValidateResponseException(ResponseExceptionType.LEGAL_ACT_BLANK_LINK);
        }
    }

    private void checkUnique(LegalActDTO dto) {
        if (uniqueChecker.existsTrimLink(dto.getLink(), dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.LEGAL_ACT_LINK_EXISTS);
        }
        if (uniqueChecker.existsTrimNameAndDateOf(dto.getName(), dto.getDateOf(), dto.getId())) {
            throw new ValidateResponseException(ResponseExceptionType.LEGAL_ACT_NAME_WITH_DATE_OF_EXISTS);
        }
    }

}
