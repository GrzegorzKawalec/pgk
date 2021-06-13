package pl.gkawalec.pgk.database.account.role;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gkawalec.pgk.common.jpa.BaseNumberIDRepository;
import pl.gkawalec.pgk.common.type.Authority;

import java.util.List;

public interface RoleRepository extends BaseNumberIDRepository<RoleEntity> {

    String SQL_EXISTS_WITHOUT_MULTIPLE_WHITE_SPACE_AND_IGNORE_CASE = "select" +
            " case when count(r) > 0 then true else false end" +
            " from RoleEntity r" +
            " where upper(:name) = regexp_replace(upper(r.name), '(\\s){2,}', ' ', 'g')";

    RoleEntity findByName(String name);

    List<RoleEntity> findAllByOrderByName();

    List<RoleEntity> findAllByAuthorities_authority(Authority authority);

    @Query(SQL_EXISTS_WITHOUT_MULTIPLE_WHITE_SPACE_AND_IGNORE_CASE)
    boolean existsByNameWithoutMultipleWhiteSpaceAndIgnoreCase(@Param("name") String name);

    @Query(SQL_EXISTS_WITHOUT_MULTIPLE_WHITE_SPACE_AND_IGNORE_CASE + " and r.id != :excludedId")
    boolean existsByNameWithoutMultipleWhiteSpaceAndIgnoreCaseExcludedId(@Param("name") String name,
                                                                         @Param("excludedId") Integer excludedId);

}
