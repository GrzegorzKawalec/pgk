package pl.gkawalec.pgk.application.legalact;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.test.annotation.PGKSpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PGKSpringBootTest
class LegalActUniqueCheckerTest {

    private final String generatedLegalActName = "Legal Act I";
    private final LocalDate generatedLegalActDateOf = LocalDate.now();
    private final String generatedLegalActLink = "www.legal-act-1.pl";

    @Autowired
    private LegalActUniqueChecker uniqueChecker;

    @Autowired
    private TestLegalActCreator legalActCreator;

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a name in the database (should not exist)")
    void existsTrimNameAndDateOf_UUID() {
        //given
        String nameUUID = UUID.randomUUID().toString();

        //When
        boolean exists = uniqueChecker.existsTrimNameAndDateOf(nameUUID, LocalDate.now(), null);

        //then
        assertFalse(exists, "The generated UUID should not exist as a name in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the name (with date of) that exists in the database")
    void existsTrimNameAndDateOf_exists() {
        //given
        legalActCreator.createForNameAndDateOf(generatedLegalActName, generatedLegalActDateOf);

        //when
        boolean exists = uniqueChecker.existsTrimNameAndDateOf(generatedLegalActName, generatedLegalActDateOf, null);

        //then
        assertTrue(exists, "The name (with date of) being checked exists in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the name (with date of) that exists in the database (extra spaces and upper case)")
    void existsTrimNameAndDateOf_existsNameWithAdditionalSpace() {
        //given
        String checkedName = " " + generatedLegalActName.replace(" ", "    ").toUpperCase() + "    ";
        legalActCreator.createForNameAndDateOf(generatedLegalActName, generatedLegalActDateOf);

        //when
        boolean exists = uniqueChecker.existsTrimNameAndDateOf(checkedName, generatedLegalActDateOf, null);

        //then
        assertTrue(exists, "The name (with date of) being checked contains extra spaces and is upper case, but the method should return true anyway");
    }

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a name (with date of) in the database, along with the id of an existing legal act (should not exist)")
    void existsTrimNameAndDateOf_UUIDWithLegalActId() {
        //given
        String nameUUID = UUID.randomUUID().toString();
        LegalActEntity legalActEntity = legalActCreator.createForNameAndDateOf(generatedLegalActName, generatedLegalActDateOf);

        //when
        boolean exists = uniqueChecker.existsTrimNameAndDateOf(nameUUID, generatedLegalActDateOf, legalActEntity.getId());

        //then
        assertFalse(exists, "The generated UUID should not exist as a name (with date of) in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the name (with date of) that exists in the database, but the given identifier should exclude its checking")
    void existsTrimNameAndDateOf_theSameNameWithGivenId() {
        //given
        LegalActEntity legalActEntity = legalActCreator.createForNameAndDateOf(generatedLegalActName, generatedLegalActDateOf);

        //when
        boolean exists = uniqueChecker.existsTrimNameAndDateOf(generatedLegalActName, generatedLegalActDateOf, legalActEntity.getId());

        //then
        assertFalse(exists, "The name (with date of) is present, but the given identifier precludes its checking");
    }

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a link in the database (should not exist)")
    void existsTrimLink_UUID() {
        //given
        String linkUUID = "www." + UUID.randomUUID() + ".pl";

        //when
        boolean exists = uniqueChecker.existsTrimLink(linkUUID, null);

        //then
        assertFalse(exists, "The generated UUID should not exist as link in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the link that exists in the database")
    void existsTrimLink_exists() {
        //given
        legalActCreator.createForLink(generatedLegalActLink);

        //when
        boolean exists = uniqueChecker.existsTrimLink(generatedLegalActLink, null);

        //then
        assertTrue(exists, "The link being checked exists in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the link that exists in the database (extra space at the beginning and end)")
    void existsTrimLink_existsWithAdditionalSpace() {
        //given
        String checkedLink = "   " + generatedLegalActLink + "  ";
        legalActCreator.createForLink(generatedLegalActLink);

        //when
        boolean exists = uniqueChecker.existsTrimLink(checkedLink, null);

        //then
        assertTrue(exists, "The link being checked contains extra spaces, but the method should return true anyway");
    }

    @Test
    @Transactional
    @DisplayName("Check if the generated UUID exists as a link in the database, along with the id of an existing legal act (should not exist)")
    void existsTrimLink_UUIDWithLegalActId() {
        //given
        String linkUUID = "www." + UUID.randomUUID() + ".pl";
        LegalActEntity legalActEntity = legalActCreator.createForLink(generatedLegalActLink);

        //when
        boolean exists = uniqueChecker.existsTrimLink(linkUUID, legalActEntity.getId());

        //then
        assertFalse(exists, "The generated UUID should not exist as a link in the database");
    }

    @Test
    @Transactional
    @DisplayName("Check the link that exists in the database, but the given identifier should exclude its checking")
    void existsTrimLink_theSameNameWithGivenId() {
        //given
        LegalActEntity legalActEntity = legalActCreator.createForLink(generatedLegalActLink);

        //when
        boolean exists = uniqueChecker.existsTrimLink(generatedLegalActLink, legalActEntity.getId());

        //then
        assertFalse(exists, "The link is present, but the given identifier precludes its checking");
    }

}
