package com.loan24.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {

    public ResourceNotFoundException(Class<?> clazz, final Long id) {
        super(HttpStatus.NOT_FOUND, String.format("reason: cannot find resource type: %s by identifier: %s", clazz.getSimpleName(), id));
    }

    public ResourceNotFoundException(Class<?> clazz, final String name) {
        super(HttpStatus.NOT_FOUND, String.format("reason: cannot find resource type: %s by name: %s", clazz.getSimpleName(), name));
    }
}
