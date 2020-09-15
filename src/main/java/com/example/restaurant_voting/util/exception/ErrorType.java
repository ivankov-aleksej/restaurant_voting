package com.example.restaurant_voting.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    //  http://stackoverflow.com/a/22358422/548473
    DATA_NOT_FOUND("Data not found", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_ERROR("Data error", HttpStatus.UNPROCESSABLE_ENTITY),
    VALIDATION_ERROR("Validation error", HttpStatus.UNPROCESSABLE_ENTITY),
    UNIQUE_ERROR("Unique error", HttpStatus.CONFLICT),
    WRONG_REQUEST("Wrong request", HttpStatus.BAD_REQUEST),
    EXPIRED_DATE("Expired date", HttpStatus.UNPROCESSABLE_ENTITY),
    EXPIRED_TIME("Expired time", HttpStatus.UNPROCESSABLE_ENTITY);

    private final String errorCode;
    private final HttpStatus status;

    ErrorType(String errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
