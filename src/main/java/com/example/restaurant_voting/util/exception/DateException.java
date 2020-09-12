package com.example.restaurant_voting.util.exception;

public class DateException extends RuntimeException {
    private static final String DATE_EXPIRED_EXCEPTION = "date is expired for entity with";

    public DateException(String arg) {
        super(DATE_EXPIRED_EXCEPTION + arg);
    }
}
