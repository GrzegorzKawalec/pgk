package pl.gkawalec.pgk.common.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class StringUtil {

    public boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public boolean isBlank(String str) {
        return org.apache.commons.lang3.StringUtils.isBlank(str);
    }

    public String trim(String str) {
        return Objects.isNull(str) ? null : str.trim();
    }

    public String changeFirstLetterToLowercase(String str) {
        if (isBlank(str)) return str;
        char[] allChars = str.toCharArray();
        char fistChar = allChars[0];
        allChars[0] = Character.toLowerCase(fistChar);
        return new String(allChars);
    }

    public String removeMultipleWhiteSpace(String str) {
        if (Objects.isNull(str)) {
            return null;
        }
        return str.replaceAll("(\\s){2,}", " ");
    }

    public String trimAndRemoveMultipleWhiteSpace(String str) {
        str = trim(str);
        return removeMultipleWhiteSpace(str);
    }

}
