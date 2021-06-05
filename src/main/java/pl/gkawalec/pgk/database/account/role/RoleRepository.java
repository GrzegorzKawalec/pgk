package pl.gkawalec.pgk.database.account.role;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;

public interface RoleRepository extends BaseNumberIDRepository<RoleEntity> {

    String SQL_EXISTS_WITHOUT_MULTIPLE_WHITE_SPACE_AND_IGNORE_CASE = "select" +
            " case when count(r) > 0 then true else false end" +
            " from RoleEntity r" +
            " where upper(:name) = regexp_replace(upper(r.name), '(\\s){2,}', ' ', 'g')";

    RoleEntity findByName(String name);

    @Query(SQL_EXISTS_WITHOUT_MULTIPLE_WHITE_SPACE_AND_IGNORE_CASE)
    boolean existsWithoutMultipleWhiteSpaceAndIgnoreCase(@Param("name") String name);

    @Query(SQL_EXISTS_WITHOUT_MULTIPLE_WHITE_SPACE_AND_IGNORE_CASE + " and r.id != :excludedId")
    boolean existsWithoutMultipleWhiteSpaceAndIgnoreCaseExcludedId(@Param("name") String name,
                                                                   @Param("excludedId") Integer excludedId);

}
