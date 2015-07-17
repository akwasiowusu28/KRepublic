package com.republic.support.exceptions;

public class ArgumentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Throwable innerException) {
        super(message, innerException);
    }
}
