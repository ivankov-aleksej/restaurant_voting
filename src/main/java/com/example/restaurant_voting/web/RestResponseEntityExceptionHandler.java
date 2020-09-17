package com.example.restaurant_voting.web;

import com.example.restaurant_voting.util.ValidationUtil;
import com.example.restaurant_voting.util.exception.ApplicationException;
import com.example.restaurant_voting.util.exception.ErrorInfo;
import com.example.restaurant_voting.util.exception.ErrorType;
import com.example.restaurant_voting.util.exception.IllegalRequestDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Objects;

import static com.example.restaurant_voting.util.exception.ErrorType.*;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<String, String> CONSTRAINS_MAP = Map.of(
            "(restaurant_id, created_on)", "menu already existed",
            "(date, user_id)", "vote already existed",
            "(email)", "email already existed",
            "(restaurant_id, action_date)", "menu already existed",
            "(name)", "restaurant already existed");


    //422
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        String[] strings = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toArray(String[]::new);

        ErrorInfo errorInfo = logAndGetErrorInfo(request, ex, false, VALIDATION_ERROR, strings);
        return handleExceptionInternal(ex, errorInfo, new HttpHeaders(), VALIDATION_ERROR.getStatus(), request);
    }

    //422
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleViolationException(ConstraintViolationException ex, WebRequest request) {
        String[] strings = ex.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath().toString() + " : " + constraintViolation.getMessage())
                .toArray(String[]::new);

        ErrorInfo errorInfo = logAndGetErrorInfo(request, ex, false, VALIDATION_ERROR, strings);
        return handleExceptionInternal(ex, errorInfo, new HttpHeaders(), VALIDATION_ERROR.getStatus(), request);
    }

    //400
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String substringEx = Objects.requireNonNull(ex.getMessage()).substring(0, ex.getMessage().indexOf("; nested exception"));
        ErrorInfo errorInfo = logAndGetErrorInfo(request, ex, false, WRONG_REQUEST, substringEx);
        return handleExceptionInternal(ex, errorInfo, new HttpHeaders(), WRONG_REQUEST.getStatus(), request);
    }

    //422
    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> applicationError(ApplicationException ex, WebRequest request) {
        ErrorInfo errorInfo = logAndGetErrorInfo(request, ex, false, ex.getType(), ex.getMsgCodeAndArgs());
        return handleExceptionInternal(ex, errorInfo, new HttpHeaders(), ex.getType().getStatus(), request);
    }

    //422
    @ExceptionHandler({IllegalRequestDataException.class})
    public ResponseEntity<Object> illegalRequestDataError(IllegalRequestDataException ex, WebRequest request) {
        ErrorInfo errorInfo = logAndGetErrorInfo(request, ex, false, VALIDATION_ERROR);
        return handleExceptionInternal(ex, errorInfo, new HttpHeaders(), VALIDATION_ERROR.getStatus(), request);
    }

    //409 or 422
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> conflict(final DataIntegrityViolationException ex, final WebRequest request) {
        String rootMsg = ValidationUtil.getRootCause(ex).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return handleExceptionInternal(ex, logAndGetErrorInfo(request, ex, false, UNIQUE_ERROR, entry.getValue()),
                            new HttpHeaders(), UNIQUE_ERROR.getStatus(), request);
                }
            }
        }
        return handleExceptionInternal(ex, logAndGetErrorInfo(request, ex, true, DATA_ERROR),
                new HttpHeaders(), DATA_ERROR.getStatus(), request);
    }

    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception ex, WebRequest request) {
        ErrorInfo errorInfo = logAndGetErrorInfo(request, ex, true, APP_ERROR);
        return handleExceptionInternal(ex, errorInfo, new HttpHeaders(), APP_ERROR.getStatus(), request);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private ErrorInfo logAndGetErrorInfo(WebRequest req, Exception e, boolean logException, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logException, errorType);
        return new ErrorInfo(((ServletWebRequest) req).getRequest().getRequestURI(), errorType, errorType.getErrorCode(),
                details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)});
    }
}
