package pl.gkawalec.pgk.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    private final String nullString = null;
    private final String emptyString = "";
    private final String blankString = "    ";

    private final String newLineString1 = "\n";
    private final String newLineString2 = "\r";
    private final String newLineString3 = "\n\r";
    private final String tabString = "\t";

    private final String notBlankString1 = "String";
    private final String notBlankString2 = "   ,   ";
    private final String notBlankString3 = "\na\n";
    private final String notBlankString4 = "\ta\r";
    private final String notBlankString5 = "\tb";
    private final String notBlankString6 = "b\n";


    @Test
    @DisplayName("Check if the given string is blank")
    void isNotBlank() {
        //when
        boolean nullString = StringUtil.isNotBlank(this.nullString);
        boolean emptyString = StringUtil.isNotBlank(this.emptyString);
        boolean blankString = StringUtil.isNotBlank(this.blankString);

        boolean newLineString1 = StringUtil.isNotBlank(this.newLineString1);
        boolean newLineString2 = StringUtil.isNotBlank(this.newLineString2);
        boolean newLineString3 = StringUtil.isNotBlank(this.newLineString3);
        boolean tabString = StringUtil.isNotBlank(this.tabString);

        boolean notBlankString1 = StringUtil.isNotBlank(this.notBlankString1);
        boolean notBlankString2 = StringUtil.isNotBlank(this.notBlankString2);
        boolean notBlankString3 = StringUtil.isNotBlank(this.notBlankString3);
        boolean notBlankString4 = StringUtil.isNotBlank(this.notBlankString4);
        boolean notBlankString5 = StringUtil.isNotBlank(this.notBlankString5);
        boolean notBlankString6 = StringUtil.isNotBlank(this.notBlankString6);

        //then
        assertFalse(nullString, "It's null String");
        assertFalse(emptyString, "It's empty String");
        assertFalse(blankString, "It's blank String");

        assertFalse(newLineString1, "It's String with only one char: \"n");
        assertFalse(newLineString2, "It's String with only one char: \"r");
        assertFalse(newLineString3, "It's String with only two chars: \"n\"r");
        assertFalse(tabString, "It's String with only one char: \"t");

        assertTrue(notBlankString1, "It's not blank String: " + notBlankString1);
        assertTrue(notBlankString2, "It's not blank String: " + notBlankString2);
        assertTrue(notBlankString3, "It's not blank String: " + notBlankString3);
        assertTrue(notBlankString4, "It's not blank String: " + notBlankString4);
        assertTrue(notBlankString5, "It's not blank String: " + notBlankString5);
        assertTrue(notBlankString6, "It's not blank String: " + notBlankString6);
    }

    @Test
    @DisplayName("Check if the given string is not blank")
    void isBlank() {
        //when
        boolean nullString = StringUtil.isBlank(this.nullString);
        boolean emptyString = StringUtil.isBlank(this.emptyString);
        boolean blankString = StringUtil.isBlank(this.blankString);

        boolean newLineString1 = StringUtil.isBlank(this.newLineString1);
        boolean newLineString2 = StringUtil.isBlank(this.newLineString2);
        boolean newLineString3 = StringUtil.isBlank(this.newLineString3);
        boolean tabString = StringUtil.isBlank(this.tabString);

        boolean notBlankString1 = StringUtil.isBlank(this.notBlankString1);
        boolean notBlankString2 = StringUtil.isBlank(this.notBlankString2);
        boolean notBlankString3 = StringUtil.isBlank(this.notBlankString3);
        boolean notBlankString4 = StringUtil.isBlank(this.notBlankString4);
        boolean notBlankString5 = StringUtil.isBlank(this.notBlankString5);
        boolean notBlankString6 = StringUtil.isBlank(this.notBlankString6);

        //then
        assertTrue(nullString, "It's null String");
        assertTrue(emptyString, "It's empty String");
        assertTrue(blankString, "It's blank String");

        assertTrue(newLineString1, "It's String with only one char: \"n");
        assertTrue(newLineString2, "It's String with only one char: \"r");
        assertTrue(newLineString3, "It's String with only two chars: \"n\"r");
        assertTrue(tabString, "It's String with only one char: \"t");

        assertFalse(notBlankString1, "It's not blank String: " + notBlankString1);
        assertFalse(notBlankString2, "It's not blank String: " + notBlankString2);
        assertFalse(notBlankString3, "It's not blank String: " + notBlankString3);
        assertFalse(notBlankString4, "It's not blank String: " + notBlankString4);
        assertFalse(notBlankString5, "It's not blank String: " + notBlankString5);
        assertFalse(notBlankString6, "It's not blank String: " + notBlankString6);
    }

    @Test
    @DisplayName("Check that white space at the beginning and end of the string are removed")
    void trim() {
        //given
        String singleChar_init = "s";
        String singleWord_init = "String";
        String notForTrim_init = singleWord_init + "    " + singleWord_init;
        String startToTrim_init = "    " + singleWord_init;
        String endToTrim_init = singleWord_init + "   ";
        String startAndEndToTrim_init = "    " + singleWord_init + "   ";

        //when
        String nullString = StringUtil.trim(this.nullString);
        String emptyString = StringUtil.trim(this.emptyString);
        String blankString = StringUtil.trim(this.blankString);

        String newLineString1 = StringUtil.trim(this.newLineString1);
        String newLineString2 = StringUtil.trim(this.newLineString2);
        String newLineString3 = StringUtil.trim(this.newLineString3);
        String tabString = StringUtil.trim(this.tabString);

        String singleChar = StringUtil.trim(singleChar_init);
        String singleWord = StringUtil.trim(singleWord_init);
        String notForTrim = StringUtil.trim(notForTrim_init);
        String startToTrim = StringUtil.trim(startToTrim_init);
        String endToTrim = StringUtil.trim(endToTrim_init);
        String startAndEndToTrim = StringUtil.trim(startAndEndToTrim_init);

        //then
        assertNull(nullString, "It's null String");
        assertEquals("", emptyString, "It's empty String");
        assertEquals("", blankString, "It's blank String");

        assertEquals("", newLineString1, "It's String with only one char: \"n");
        assertEquals("", newLineString2, "It's String with only one char: \"r");
        assertEquals("", newLineString3, "It's String with only two chars: \"n\"r");
        assertEquals("", tabString, "It's String with only one char: \"t");

        assertEquals(singleChar_init, singleChar, "This is a single letter without whitespace at the beginning and end");
        assertEquals(singleWord_init, singleWord, "This is a single word without whitespace at the beginning and end");
        assertEquals(notForTrim_init, notForTrim, "This String has no whitespace at the beginning or end");
        assertEquals(singleWord_init, startToTrim, "This String of characters starts with whitespace");
        assertEquals(singleWord_init, endToTrim, "This String has whitespace at the end");
        assertEquals(singleWord_init, startAndEndToTrim, "This String has whitespace at the beginning and end");
    }

    @Test
    @DisplayName("Check that the first letter of the given string will be changed to lowercase")
    void changeFirstLetterToLowercase() {
        //given
        String onlyNumber_init = "123";
        String firstUpper_init = "Pl";
        String firLower_init = "pL";
        String singleUpper_init = "P";
        String singleLower_init = "p";

        //when
        String nullString = StringUtil.changeFirstLetterToLowercase(this.nullString);
        String emptyString = StringUtil.changeFirstLetterToLowercase(this.emptyString);
        String blankString = StringUtil.changeFirstLetterToLowercase(this.blankString);

        String newLineString1 = StringUtil.changeFirstLetterToLowercase(this.newLineString1);
        String newLineString2 = StringUtil.changeFirstLetterToLowercase(this.newLineString2);
        String newLineString3 = StringUtil.changeFirstLetterToLowercase(this.newLineString3);
        String tabString = StringUtil.changeFirstLetterToLowercase(this.tabString);

        String onlyNumber = StringUtil.changeFirstLetterToLowercase(onlyNumber_init);
        String firstUpper = StringUtil.changeFirstLetterToLowercase(firstUpper_init);
        String firLower = StringUtil.changeFirstLetterToLowercase(firLower_init);
        String singleUpper = StringUtil.changeFirstLetterToLowercase(singleUpper_init);
        String singleLower = StringUtil.changeFirstLetterToLowercase(singleLower_init);

        //then
        assertNull(nullString, "It's null String");
        assertEquals(this.emptyString, emptyString, "It's empty String");
        assertEquals(this.blankString, blankString, "It's blank String");

        assertEquals(this.newLineString1, newLineString1, "It's String with only one char: \"n");
        assertEquals(this.newLineString2, newLineString2, "It's String with only one char: \"r");
        assertEquals(this.newLineString3, newLineString3, "It's String with only two chars: \"n\"r");
        assertEquals(this.tabString, tabString, "It's String with only one char: \"t");

        assertEquals("123", onlyNumber, "It's only number");
        assertEquals("pl", firstUpper, "The first letter was capitalized");
        assertEquals("pL", firLower, "The first letter was already in capital");
        assertEquals("p", singleUpper, "This is one capital letter");
        assertEquals("p", singleLower, "One letter was not capital");
    }

    @Test
    @DisplayName("Check that any excess spaces between words have been removed")
    void removeMultipleWhiteSpace() {
        //given
        String oneWord_init = "word";
        String needsNoChange_init = "word word word";
        String needOneChange_init = " word     word word ";
        String needTwoChange_init = "  word word  ";

        String blankString_correctResult = " ";
        String needOneChange_correctResult = " word word word ";
        String needTwoChange_correctResult = " word word ";

        //when
        String nullString = StringUtil.removeMultipleWhiteSpace(this.nullString);
        String emptyString = StringUtil.removeMultipleWhiteSpace(this.emptyString);
        String blankString = StringUtil.removeMultipleWhiteSpace(this.blankString);
        String oneWord = StringUtil.removeMultipleWhiteSpace(oneWord_init);
        String needsNoChange = StringUtil.removeMultipleWhiteSpace(needsNoChange_init);
        String needOneChange = StringUtil.removeMultipleWhiteSpace(needOneChange_init);
        String needTwoChange = StringUtil.removeMultipleWhiteSpace(needTwoChange_init);

        //then
        assertNull(nullString, "It's null String");
        assertEquals(this.emptyString, emptyString, "It's empty String");
        assertEquals(blankString_correctResult, blankString, "The string has a few spaces and should result in one space");
        assertEquals(oneWord_init, oneWord, "A single word needs no modification");
        assertEquals(needsNoChange_init, needsNoChange, "The sentence doesn't have too many spaces between words");
        assertEquals(needOneChange_correctResult, needOneChange);
        assertEquals(needTwoChange_correctResult, needTwoChange);
    }

    @Test
    @DisplayName("Check that any excess spaces between words and at the beginning and end of the sentence have been removed")
    void trimAndRemoveMultipleWhiteSpace() {
        //given
        String oneWord_init = "word";
        String needsNoChange_init = "word word word";
        String needTwoChange_init = "  word word  ";
        String needThreeChange_init = " word     word word ";

        String blankString_correctResult = "";
        String needTwoChange_correctResult = "word word";
        String needThreeChange_correctResult = "word word word";

        //when
        String nullString = StringUtil.trimAndRemoveMultipleWhiteSpace(this.nullString);
        String emptyString = StringUtil.trimAndRemoveMultipleWhiteSpace(this.emptyString);
        String blankString = StringUtil.trimAndRemoveMultipleWhiteSpace(this.blankString);
        String oneWord = StringUtil.trimAndRemoveMultipleWhiteSpace(oneWord_init);
        String needsNoChange = StringUtil.trimAndRemoveMultipleWhiteSpace(needsNoChange_init);
        String needTwoChange = StringUtil.trimAndRemoveMultipleWhiteSpace(needTwoChange_init);
        String needThreeChange = StringUtil.trimAndRemoveMultipleWhiteSpace(needThreeChange_init);

        //then
        assertNull(nullString, "It's null String");
        assertEquals(this.emptyString, emptyString, "It's empty String");
        assertEquals(blankString_correctResult, blankString, "The string has a few spaces and no space should be left as a result");
        assertEquals(oneWord_init, oneWord, "A single word needs no modification");
        assertEquals(needsNoChange_init, needsNoChange, "The sentence doesn't have too many spaces between words");
        assertEquals(needTwoChange_correctResult, needTwoChange);
        assertEquals(needThreeChange_correctResult, needThreeChange);
    }

}
