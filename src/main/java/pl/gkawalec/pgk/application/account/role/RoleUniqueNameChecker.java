package pl.gkawalec.pgk.application.account.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.role.RoleRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
class RoleUniqueNameChecker {

    private final RoleRepository roleRepository;

    boolean existsTrimName(String roleName, Integer excludedRoleId) {
        if (Objects.isNull(excludedRoleId)) {
            return existsTrimName(roleName);
        }
        roleName = StringUtil.trimAndRemoveMultipleWhiteSpace(roleName);
        if (StringUtil.isBlank(roleName)) {
            return false;
        }
        return roleRepository.existsWithoutMultipleWhiteSpaceAndIgnoreCaseExcludedId(roleName, excludedRoleId);
    }

    boolean existsTrimName(String roleName) {
        roleName = StringUtil.trimAndRemoveMultipleWhiteSpace(roleName);
        if (StringUtil.isBlank(roleName)) {
            return false;
        }
        return roleRepository.existsWithoutMultipleWhiteSpaceAndIgnoreCase(roleName);
    }

}
