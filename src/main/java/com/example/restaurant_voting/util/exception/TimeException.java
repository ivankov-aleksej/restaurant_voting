package com.example.restaurant_voting.util.exception;

public class TimeException extends ApplicationException {
    private static final String EXPIRED_TIME_FOR_VOTING_EXCEPTION = "Expired time for voting";

    public TimeException() {
        super(ErrorType.EXPIRED_TIME, EXPIRED_TIME_FOR_VOTING_EXCEPTION, "");
    }
}
