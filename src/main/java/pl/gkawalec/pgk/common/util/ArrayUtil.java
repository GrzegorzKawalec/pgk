package pl.gkawalec.pgk.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

@UtilityClass
public class ArrayUtil {

    public <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public <T> boolean isEmpty(T[] array) {
        return ArrayUtils.isEmpty(array);
    }

}
