package com.daniilmiskevich.labs.exceptions.exception;

import jakarta.validation.ValidationException;

public class InvalidRangeException extends ValidationException {
    public InvalidRangeException(Object start, Object end) {
        super(String.format("Invalid range: %s..%s.", start, end));
    }
}
