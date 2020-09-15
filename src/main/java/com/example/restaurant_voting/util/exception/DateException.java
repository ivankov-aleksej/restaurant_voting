package com.example.restaurant_voting.util.exception;

public class DateException extends ApplicationException {
    private static final String EXPIRED_DATE_EXCEPTION = "Expired date for entity with ";

    public DateException(String arg) {
        super(ErrorType.EXPIRED_DATE, EXPIRED_DATE_EXCEPTION, arg);
    }
}
