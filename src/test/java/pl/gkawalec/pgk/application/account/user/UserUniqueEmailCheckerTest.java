package pl.gkawalec.pgk.application.account.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;
import pl.gkawalec.pgk.test.util.TestUserUtil;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PGKSpringBootTest
class UserUniqueEmailCheckerTest {

    private final String generatedEmail = "nameNameName@nameNameName";

    @Autowired
    private UserUniqueEmailChecker uniqueEmailChecker;

    @Autowired
    private TestUserUtil testUserUtil;

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a email in the database (should not exist)")
    void existsTrimEmail_UUID() {
        //given
        String uuid = UUID.randomUUID().toString();
        String email = uuid + "@" + uuid;

        //when
        boolean exists = uniqueEmailChecker.existsTrimEmail(email);

        //then
        assertFalse(exists, "The generated UUID should not exist as a email in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the email that exists in the database")
    void existsTrimName_exists() {
        //given
        testUserUtil.insertUser(generatedEmail);

        //when
        boolean exists = uniqueEmailChecker.existsTrimEmail(generatedEmail);

        //then
        assertTrue(exists, "The email being checked exists in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the email that exists in the database (with extra spaces)")
    void existsTrimName_existsWithAdditionalSpace() {
        //given
        String email = " " + generatedEmail + "    ";
        testUserUtil.insertUser(generatedEmail);

        //when
        boolean exists = uniqueEmailChecker.existsTrimEmail(email);

        //then
        assertTrue(exists, "The email being checked contains extra spaces, but the method should return true anyway");
    }

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a email in the database, along with the id of an existing user (should not exist)")
    void existsTrimName_UUIDWithUserId() {
        //given
        String uuid = UUID.randomUUID().toString();
        String email = uuid + "@" + uuid;
        UserEntity userEntity = testUserUtil.insertUser(generatedEmail);

        //when
        boolean exists = uniqueEmailChecker.existsTrimEmail(email, userEntity.getId());

        //then
        assertFalse(exists, "The generated UUID should not exist as a email in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the email that exists in the database, but the given identifier should exclude its checking")
    void existsTrimName_theSameEmailWithGivenId() {
        //given
        UserEntity userEntity = testUserUtil.insertUser(generatedEmail);

        //when
        boolean exists = uniqueEmailChecker.existsTrimEmail(generatedEmail, userEntity.getId());

        //then
        assertFalse(exists, "The email is present, but the given identifier precludes its checking");
    }

}
