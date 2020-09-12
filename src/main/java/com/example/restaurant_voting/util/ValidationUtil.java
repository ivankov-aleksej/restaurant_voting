package com.example.restaurant_voting.util;

import com.example.restaurant_voting.util.exception.DateException;
import com.example.restaurant_voting.util.exception.NotFoundException;
import com.example.restaurant_voting.util.exception.TimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String arg) {
        if (!found) {
            throw new NotFoundException(arg);
        }
    }

    public static void checkExpiredDate(LocalDate date, int id) {
        if (!date.isAfter(DateUtil.getDate())) {
            throw new DateException("id=" + id);
        }
    }

    public static void checkCurrentDate(LocalDate date, int id) {
        if (!date.isEqual(DateUtil.getDate())) {
            throw new DateException("id=" + id);
        }
    }

    public static void checkExpiredTime(LocalTime current, LocalTime template) {
        if (current.isAfter(template)) {
            throw new TimeException();
        }
    }

}
