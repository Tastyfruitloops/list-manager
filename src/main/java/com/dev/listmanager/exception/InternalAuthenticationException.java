package com.dev.listmanager.exception;

public class InternalAuthenticationException extends RuntimeException {
    public InternalAuthenticationException() {
        super();
    }

    public InternalAuthenticationException(String message) {
        super(message);
    }

    public InternalAuthenticationException(Throwable cause) {
        super(cause);
    }
}
