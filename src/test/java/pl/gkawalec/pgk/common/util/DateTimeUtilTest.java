package pl.gkawalec.pgk.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateTimeUtilTest {

    private final int year = 2021;
    private final Month month = Month.JUNE;
    private final int day = 5;

    private final int hour = 15;
    private final int minute = 20;
    private final int second = 33;

    private final LocalDateTime setDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    private final LocalDateTime now = LocalDateTime.now();

    private final String setDate = parseSetDate();
    private final String setTime = parseSetTime();
    private final String nowDate = parseNowDate();
    private final String nowTime = parseNowTime();

    @Test
    @DisplayName("Check date parsing")
    void dateToString() {
        //when
        String nullDate = DateTimeUtil.dateToString(null);
        String setDate = DateTimeUtil.dateToString(this.setDateTime);
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
        String setTime = DateTimeUtil.timeToString(this.setDateTime);
        String nowTime = DateTimeUtil.timeToString(this.now);

        //then
        assertNull(nullTime);
        assertEquals(this.setTime, setTime);
        assertEquals(this.nowTime, nowTime);
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
        return year + "-" + prepareTwoElementOfTimeOrDate(month) + "-" + prepareTwoElementOfTimeOrDate(day);
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
