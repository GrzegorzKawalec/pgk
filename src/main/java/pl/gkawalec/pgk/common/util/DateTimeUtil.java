package pl.gkawalec.pgk.common.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@UtilityClass
public class DateTimeUtil {

    public String dateToString(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.toString();
    }

    public String timeToString(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        LocalTime localTime = localDateTime.toLocalTime();
        return localTime.withNano(0).toString();
    }

}
