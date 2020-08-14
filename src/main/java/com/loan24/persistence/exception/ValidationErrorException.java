package com.loan24.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationErrorException extends ResponseStatusException {

    public ValidationErrorException(Class<?> clazz, String field, String reason) {
        super(HttpStatus.BAD_REQUEST, String.format("validation error on class: %s, field: %s, message: %s", clazz.getSimpleName(), field, reason));
    }
}
