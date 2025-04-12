package com.daniilmiskevich.labs.exceptions.handler;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.daniilmiskevich.labs.exceptions.model.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        ValidationException.class,
        HandlerMethodValidationException.class,

        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        TypeMismatchException.class,
    })
    public ResponseEntity<ApiError> handleValidationException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(e));
    }

    @ExceptionHandler({
        EntityNotFoundException.class,
        NoSuchElementException.class,
    })
    public ResponseEntity<ApiError> handleEntityNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError(e));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiError(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError(
                e.toString(),
                List.of(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()));
    }

}
