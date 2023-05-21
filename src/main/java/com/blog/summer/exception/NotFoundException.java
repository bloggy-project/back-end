package com.blog.summer.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Throwable cause) {
        super(cause);
    }
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
