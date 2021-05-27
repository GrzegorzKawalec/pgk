package pl.gkawalec.pgk.application.account.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.database.account.RoleEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PGKSpringBootTest
class RoleUniqueNameCheckerTest {

    private final String generatedRoleName = "Name name Name";

    @Autowired
    private RoleUniqueNameChecker uniqueNameChecker;

    @Autowired
    private TestRoleCreator roleCreator;

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a name in the database (should not exist)")
    void existsTrimName_UUID() {
        //given
        String nameUUID = UUID.randomUUID().toString();

        //When
        boolean exists = uniqueNameChecker.existsTrimName(nameUUID);

        //then
        assertFalse(exists, "The generated UUID should not exist as a name in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the name that exists in the database")
    void existsTrimName_exists() {
        //given
        roleCreator.createAdmin(generatedRoleName);

        //when
        boolean exists = uniqueNameChecker.existsTrimName(generatedRoleName);

        //then
        assertTrue(exists, "The name being checked exists in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the name that exists in the database (extra spaces and upper case)")
    void existsTrimName_existsWithAdditionalSpace() {
        //given
        String checkedName = " " + generatedRoleName.replace(" ", "    ").toUpperCase() + "    ";
        roleCreator.createAdmin(generatedRoleName);

        //when
        boolean exists = uniqueNameChecker.existsTrimName(checkedName);

        //then
        assertTrue(exists, "The name being checked contains extra spaces and is upper case, but the method should return true anyway");
    }

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a name in the database, along with the id of an existing role (should not exist)")
    void existsTrimName_UUIDWithRoleId() {
        //given
        String nameUUID = UUID.randomUUID().toString();
        RoleEntity generatedRole = roleCreator.createAdmin(generatedRoleName);

        //when
        boolean exists = uniqueNameChecker.existsTrimName(nameUUID, generatedRole.getId());

        //then
        assertFalse(exists, "The generated UUID should not exist as a name in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the name that exists in the database, but the given identifier should exclude its checking")
    void existsTrimName_theSameNameWithGivenId() {
        //given
        RoleEntity generatedRole = roleCreator.createAdmin(generatedRoleName);

        //when
        boolean exists = uniqueNameChecker.existsTrimName(generatedRoleName, generatedRole.getId());

        //then
        assertFalse(exists, "The name is present, but the given identifier precludes its checking");
    }

}
