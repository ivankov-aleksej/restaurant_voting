package com.example.restaurant_voting.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateUtil {
    private DateUtil() {
    }

    /**
     * For mock LocalDate.now()
     *
     * @return LocalDate now
     */
    public static LocalDate getDate() {
        return LocalDate.now();
    }

    public static LocalDate getTomorrow() {
        return LocalDate.now().plusDays(1);
    }

    public static LocalTime getTime() {
        return LocalTime.now();
    }
}
