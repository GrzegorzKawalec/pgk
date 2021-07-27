package pl.gkawalec.pgk.database.legalact;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

import java.time.LocalDate;
import java.util.List;

public interface LegalActRepository extends BaseNumberIDRepository<LegalActEntity> {

    String SQL_EXISTS_DATE_OF_AND_NAME_WITHOUT_MULTIPLE_SPACE_IGNORE_CASE = """
            select
             case when count(la) > 0 then true else false end
             from LegalActEntity la
             where la.dateOf = :dateOf
             and upper(:name) = regexp_replace(upper(la.name), '(\\s){2,}', ' ', 'g')
            """;

    @Query(SQL_EXISTS_DATE_OF_AND_NAME_WITHOUT_MULTIPLE_SPACE_IGNORE_CASE)
    boolean existsByDateOfAndNameWithoutMultipleWhitespaceIgnoreCase(@Param("dateOf") LocalDate dateOf,
                                                                     @Param("name") String name);

    @SuppressWarnings("SyntaxError")
    @Query(SQL_EXISTS_DATE_OF_AND_NAME_WITHOUT_MULTIPLE_SPACE_IGNORE_CASE + " and la.id != :excludedId")
    boolean existsByDateOfAndNameWithoutMultipleWhitespaceIgnoreCaseExcludedId(@Param("dateOf") LocalDate dateOf,
                                                                               @Param("name") String name,
                                                                               @Param("excludedId") Long excludedId);

    boolean existsByLinkIgnoreCase(String link);

    boolean existsByLinkIgnoreCaseAndIdNot(String link, Long excludedId);

    List<LegalActEntity> findAllByActiveIsTrue();

}
