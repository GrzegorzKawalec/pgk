package pl.gkawalec.pgk.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilTest {

    private final Object[] nullArray = null;
    private final Integer[] emptyArray = {};
    private final String[] arrayWithData = {"a", "b"};

    @Test
    @DisplayName("Check if the given arrays are not empty")
    void isNotEmpty() {
        //when
        boolean nullArray = ArrayUtil.isNotEmpty(this.nullArray);
        boolean emptyArray = ArrayUtil.isNotEmpty(this.emptyArray);
        boolean arrayWithData = ArrayUtil.isNotEmpty(this.arrayWithData);

        //then
        assertFalse(nullArray, "It's null array");
        assertFalse(emptyArray, "It's empty array");
        assertTrue(arrayWithData, "It's not empty array");
    }

    @Test
    @DisplayName("Check if the given arrays are empty")
    void isEmpty() {
        //when
        boolean nullArray = ArrayUtil.isEmpty(this.nullArray);
        boolean emptyArray = ArrayUtil.isEmpty(this.emptyArray);
        boolean arrayWithData = ArrayUtil.isEmpty(this.arrayWithData);

        //then
        assertTrue(nullArray, "It's null array");
        assertTrue(emptyArray, "It's empty array");
        assertFalse(arrayWithData, "It's not empty array");
    }
}
