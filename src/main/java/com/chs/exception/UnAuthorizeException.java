package com.chs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnAuthorizeException extends ResponseStatusException {
    public UnAuthorizeException() {
        super(HttpStatus.UNAUTHORIZED, "request unauthorized");
    }
}
