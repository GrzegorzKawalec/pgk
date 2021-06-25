package pl.gkawalec.pgk.application.account.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.user.UserRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
class UserUniqueEmailChecker {

    private final UserRepository userRepository;

    boolean existsTrimEmail(String email, Integer excludedUserId) {
        if (Objects.isNull(excludedUserId)) {
            return existsTrimEmail(email);
        }
        email = StringUtil.trim(email);
        if (StringUtil.isBlank(email)) {
            return false;
        }
        return userRepository.existsByEmailAndIdNot(email, excludedUserId);
    }

    boolean existsTrimEmail(String email) {
        email = StringUtil.trim(email);
        if (StringUtil.isBlank(email)) {
            return false;
        }
        return userRepository.existsByEmail(email);
    }

}
