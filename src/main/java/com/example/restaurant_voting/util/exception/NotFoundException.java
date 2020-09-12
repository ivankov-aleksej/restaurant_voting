package com.example.restaurant_voting.util.exception;

public class NotFoundException extends RuntimeException {
    private static final String NOT_FOUND_EXCEPTION = "Not found entity with ";

    public NotFoundException(String arg) {
        super(NOT_FOUND_EXCEPTION + arg);
    }
}