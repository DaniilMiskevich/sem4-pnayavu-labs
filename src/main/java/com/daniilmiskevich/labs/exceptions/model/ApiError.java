package com.daniilmiskevich.labs.exceptions.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import jakarta.validation.ConstraintViolationException;

public class ApiError {

    private static List<Object> getErrorsFromException(Exception exception) {
        return switch (exception) {
            case ConstraintViolationException e -> e.getConstraintViolations()
                .stream()
                .collect(Collectors.<Object>toList());
            case HandlerMethodValidationException e -> e.getParameterValidationResults()
                .stream()
                .collect(Collectors.<Object>toList());

            default -> List.of(exception.toString());
        };
    }


    private String message;
    private List<Object> errors;
    private List<String> trace;

    public ApiError(String message, List<Object> errors, List<String> trace) {
        this.message = message;
        this.errors = errors;
        this.trace = trace;
    }

    public ApiError(Exception exception) {
        this(
            exception.getMessage(),
            getErrorsFromException(exception)
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList()),
            Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .toList());
    }

    public String getMessage() {
        return message;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public List<String> getTrace() {
        return trace;
    }

}
