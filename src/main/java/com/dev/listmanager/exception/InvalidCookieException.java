package com.dev.listmanager.exception;

public class InvalidCookieException extends RuntimeException {
    public InvalidCookieException() {
        super();
    }

    public InvalidCookieException(String message) {
        super(message);
    }

    public InvalidCookieException(Throwable cause) {
        super(cause);
    }
}
