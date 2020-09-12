package com.example.restaurant_voting.util.exception;

public class TimeException extends RuntimeException {
    private static final String TIME_EXPIRED_EXCEPTION = "time is expired";

    public TimeException() {
        super(TIME_EXPIRED_EXCEPTION);
    }
}
