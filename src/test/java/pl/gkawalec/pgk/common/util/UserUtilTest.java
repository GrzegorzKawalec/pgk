package pl.gkawalec.pgk.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gkawalec.pgk.common.user.AnonymousUserAccessor;
import pl.gkawalec.pgk.common.user.SystemUserAccessor;
import pl.gkawalec.pgk.database.account.user.UserEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserUtilTest {

    @Test
    @DisplayName("Test to verify that the internal user is properly checked")
    void isInternalUser() {
        //given
        UserEntity internalUser = prepareUserEntity(UUID.randomUUID().toString());
        UserEntity systemUser = prepareUserEntity(SystemUserAccessor.IDENTIFIER);
        UserEntity systemUserAdditionalSpace = prepareUserEntity(" " + SystemUserAccessor.IDENTIFIER);
        UserEntity anonymousUser = prepareUserEntity(AnonymousUserAccessor.IDENTIFIER);
        UserEntity anonymousUserAdditionalSpace = prepareUserEntity(AnonymousUserAccessor.IDENTIFIER + " ");

        //when
        boolean isInternalUser_internal = UserUtil.isInternalUser(internalUser);
        boolean isInternalUser_system = UserUtil.isInternalUser(systemUser);
        boolean isInternalUser_systemAdditionalSpace = UserUtil.isInternalUser(systemUserAdditionalSpace);
        boolean isInternalUser_anonymous = UserUtil.isInternalUser(anonymousUser);
        boolean isInternalUser_anonymousAdditionalSpace = UserUtil.isInternalUser(anonymousUserAdditionalSpace);

        //then
        assertTrue(isInternalUser_internal);
        assertFalse(isInternalUser_system);
        assertTrue(isInternalUser_systemAdditionalSpace);
        assertFalse(isInternalUser_anonymous);
        assertTrue(isInternalUser_anonymousAdditionalSpace);
    }

    private UserEntity prepareUserEntity(String email) {
        UserEntity entity = new UserEntity();
        ReflectionTestUtils.setField(entity, "id", Integer.MAX_VALUE);
        ReflectionTestUtils.setField(entity, "email", email);
        return entity;
    }

}
