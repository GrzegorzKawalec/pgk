package pl.gkawalec.pgk.application.legalact;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.legalact.LegalActRepository;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
class LegalActUniqueChecker {

    private final LegalActRepository legalActRepository;

    boolean existsTrimNameAndDateOf(String name, LocalDate dateOf, Long excludedLegalActId) {
        if (Objects.isNull(dateOf)) {
            return false;
        }
        name = StringUtil.trimAndRemoveMultipleWhiteSpace(name);
        if (StringUtil.isBlank(name)) {
            return false;
        }
        return Objects.isNull(excludedLegalActId) ?
                legalActRepository.existsByDateOfAndNameWithoutMultipleWhitespaceIgnoreCase(dateOf, name) :
                legalActRepository.existsByDateOfAndNameWithoutMultipleWhitespaceIgnoreCaseExcludedId(dateOf, name, excludedLegalActId);
    }

    boolean existsTrimLink(String link, Long excludedLegalActId) {
        link = StringUtil.trim(link);
        if (StringUtil.isBlank(link)) {
            return false;
        }
        return Objects.isNull(excludedLegalActId) ?
                legalActRepository.existsByLinkIgnoreCase(link) :
                legalActRepository.existsByLinkIgnoreCaseAndIdNot(link, excludedLegalActId);
    }

}
