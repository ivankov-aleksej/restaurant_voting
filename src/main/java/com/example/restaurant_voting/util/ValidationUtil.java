package com.example.restaurant_voting.util;

import com.example.restaurant_voting.model.BaseEntity;
import com.example.restaurant_voting.util.exception.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundCountWithId(int objectId, int id) {
        checkNotFound(objectId != 0, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String arg) {
        if (!found) {
            throw new NotFoundException(arg);
        }
    }

    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException("must be new (id=null)");
        }
    }

    public static boolean assureIdConsistent(BaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            return false;
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(" must be with id=" + id);
        }
        return true;
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

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static Throwable logAndGetRootCause(Logger log, WebRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error("{} at request {}: {}", errorType, req.getContextPath(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getContextPath(), rootCause);
        }
        return rootCause;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

}
