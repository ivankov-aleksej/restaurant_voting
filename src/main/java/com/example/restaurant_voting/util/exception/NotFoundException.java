package com.example.restaurant_voting.util.exception;

public class NotFoundException extends ApplicationException {
    private static final String NOT_FOUND_EXCEPTION = "Not found entity with ";

    //  http://stackoverflow.com/a/22358422/548473
    public NotFoundException(String arg) {
        super(ErrorType.DATA_NOT_FOUND, NOT_FOUND_EXCEPTION, arg);
    }
}