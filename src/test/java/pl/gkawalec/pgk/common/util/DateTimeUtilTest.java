package pl.gkawalec.pgk.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateTimeUtilTest {

    private final int year = 2021;
    private final Month month = Month.JUNE;
    private final int day = 5;

    private final int hour = 15;
    private final int minute = 2;
    private final int second = 33;

    private final LocalDateTime set = LocalDateTime.of(year, month, day, hour, minute, second);
    private final LocalDateTime now = LocalDateTime.now();

    private final String setDate = parseSetDate();
    private final String setTime = parseSetTime();
    private final String setDateTime = parseSetDateTime();
    private final String nowDate = parseNowDate();
    private final String nowTime = parseNowTime();
    private final String nowDateTime = parseNowDateTime();

    @Test
    @DisplayName("Check date parsing")
    void dateToString() {
        //when
        String nullDate = DateTimeUtil.dateToString(null);
        String setDate = DateTimeUtil.dateToString(this.set);
        String nowDate = DateTimeUtil.dateToString(this.now);

        //then
        assertNull(nullDate);
        assertEquals(this.setDate, setDate);
        assertEquals(this.nowDate, nowDate);
    }

    @Test
    @DisplayName("Check time parsing")
    void timeToString() {
        //when
        String nullTime = DateTimeUtil.timeToString(null);
        String setTime = DateTimeUtil.timeToString(this.set);
        String nowTime = DateTimeUtil.timeToString(this.now);

        //then
        assertNull(nullTime);
        assertEquals(this.setTime, setTime);
        assertEquals(this.nowTime, nowTime);
    }

    @Test
    @DisplayName("Check LocalDate parsing")
    void localDateToString() {
        //when
        String nullDate = DateTimeUtil.localDateToString(null);
        String setDate = DateTimeUtil.localDateToString(this.set.toLocalDate());
        String nowDate = DateTimeUtil.localDateToString(this.now.toLocalDate());

        //then
        assertNull(nullDate);
        assertEquals(this.setDate, setDate);
        assertEquals(this.nowDate, nowDate);
    }

    @Test
    @DisplayName("Check LocalTime parsing")
    void localTimeToString() {
        //when
        String nullTime = DateTimeUtil.localTimeToString(null);
        String setTime = DateTimeUtil.localTimeToString(this.set.toLocalTime());
        String nowTime = DateTimeUtil.localTimeToString(this.now.toLocalTime());

        //then
        assertNull(nullTime);
        assertEquals(this.setTime, setTime);
        assertEquals(this.nowTime, nowTime);
    }

    @Test
    @DisplayName("Check LocalDateTime parsing")
    void localDateTimeToString() {
        //when
        String nullDateTime = DateTimeUtil.localDateTimeToString(null);
        String setDateTime = DateTimeUtil.localDateTimeToString(this.set);
        String nowDateTime = DateTimeUtil.localDateTimeToString(this.now);

        //then
        assertNull(nullDateTime);
        assertEquals(this.setDateTime, setDateTime);
        assertEquals(this.nowDateTime, nowDateTime);
    }

    @Test
    @DisplayName("Check date (String) parsing")
    void stringToLocalDate() {
        //given
        String nullDateStr = null;
        String emptyDateStr = "";
        String blankDateStr = "    ";
        String notEnoughDateElementsStr = "07.2021";
        String tooManyDateElementsStr = "10.10.07.2021";
        String badSeparatorStr = "10-07-2021";
        String badMonthStr = "10.14.2021";
        String badDayStr = "34.07.2021";
        String goodDateStr = "10.07.2021";

        //when
        LocalDate nullDate = DateTimeUtil.stringToLocalDate(nullDateStr);
        LocalDate emptyDate = DateTimeUtil.stringToLocalDate(emptyDateStr);
        LocalDate blankDate = DateTimeUtil.stringToLocalDate(blankDateStr);
        LocalDate notEnoughDateElements = DateTimeUtil.stringToLocalDate(notEnoughDateElementsStr);
        LocalDate tooManyDateElements = DateTimeUtil.stringToLocalDate(tooManyDateElementsStr);
        LocalDate badSeparator = DateTimeUtil.stringToLocalDate(badSeparatorStr);
        LocalDate badMonth = DateTimeUtil.stringToLocalDate(badMonthStr);
        LocalDate badDay = DateTimeUtil.stringToLocalDate(badDayStr);
        LocalDate goodDate = DateTimeUtil.stringToLocalDate(goodDateStr);

        //then
        assertNull(nullDate);
        assertNull(emptyDate);
        assertNull(blankDate);
        assertNull(notEnoughDateElements);
        assertNull(tooManyDateElements);
        assertNull(badSeparator);
        assertNull(badMonth);
        assertNull(badDay);
        assertEquals(DateTimeUtil.localDateToString(goodDate), goodDateStr);
    }

    private String parseSetDateTime() {
        return parseSetDate() + " " + parseSetTime();
    }

    private String parseNowDateTime() {
        return parseNowDate() + " " + parseNowTime();
    }

    private String parseSetDate() {
        return foldDate(year, month.getValue(), day);
    }

    private String parseNowDate() {
        int year = this.now.getYear();
        int month = this.now.getMonth().getValue();
        int day = this.now.getDayOfMonth();
        return foldDate(year, month, day);
    }

    private String foldDate(int year, int month, int day) {
        return prepareTwoElementOfTimeOrDate(day) + "." + prepareTwoElementOfTimeOrDate(month) + "." + year;
    }

    private String parseSetTime() {
        return foldTime(hour, minute, second);
    }

    private String parseNowTime() {
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        return foldTime(hour, minute, second);
    }

    private String foldTime(int hour, int minute, int second) {
        return prepareTwoElementOfTimeOrDate(hour) + ":" + prepareTwoElementOfTimeOrDate(minute) + ":" + prepareTwoElementOfTimeOrDate(second);
    }

    private String prepareTwoElementOfTimeOrDate(int monthOrDay) {
        return monthOrDay < 10 ?
                "0" + monthOrDay :
                "" + monthOrDay;
    }

}
