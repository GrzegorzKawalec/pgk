package pl.gkawalec.pgk.common.util;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.util.Objects;

@UtilityClass
public class DateTimeUtil {

    private static final String DATE_SEPARATOR = ".";
    private static final String TIME_SEPARATOR = ":";
    private static final String DATE_TIME_SEPARATOR = " ";
    private static final String DATE_SEPARATOR_REGEX = "[" + DateTimeUtil.DATE_SEPARATOR + "]";

    public LocalDate stringToLocalDate(String dateStr) {
        if (StringUtil.isBlank(dateStr)) {
            return null;
        }

        String[] splitDate = dateStr.split(DateTimeUtil.DATE_SEPARATOR_REGEX);
        if (splitDate.length != 3) {
            return null;
        }

        try {
            int day = Integer.parseInt(splitDate[0]);
            int month = Integer.parseInt(splitDate[1]);
            int year = Integer.parseInt(splitDate[2]);
            return LocalDate.of(year, Month.of(month), day);
        } catch (NumberFormatException | DateTimeException ex) {
            return null;
        }
    }

    public String dateToString(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        LocalDate localDate = localDateTime.toLocalDate();
        return localDateToString(localDate);
    }

    public String timeToString(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        LocalTime localTime = localDateTime.toLocalTime();
        return localTimeToString(localTime);
    }

    public String localDateTimeToString(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        return localDateToString(localDateTime.toLocalDate()) +
                DateTimeUtil.DATE_TIME_SEPARATOR +
                localTimeToString(localDateTime.toLocalTime());
    }

    public String localDateToString(LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            return null;
        }
        int day = localDate.getDayOfMonth();
        int mont = localDate.getMonth().getValue();
        int year = localDate.getYear();
        return appendZeroIfNeed(day) +
                DateTimeUtil.DATE_SEPARATOR +
                appendZeroIfNeed(mont) +
                DateTimeUtil.DATE_SEPARATOR +
                year;
    }

    public String localTimeToString(LocalTime localTime) {
        if (Objects.isNull(localTime)) {
            return null;
        }
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int second = localTime.getSecond();
        return appendZeroIfNeed(hour) +
                DateTimeUtil.TIME_SEPARATOR +
                appendZeroIfNeed(minute) +
                DateTimeUtil.TIME_SEPARATOR +
                appendZeroIfNeed(second);
    }

    private String appendZeroIfNeed(int val) {
        String prefix = val < 10 ? "0" : "";
        return prefix + val;
    }

}
