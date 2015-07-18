package com.republic.support.exceptions;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public class CloudRequestFailedException extends RuntimeException {

    public CloudRequestFailedException(String message) {
        super(message);
    }

    public CloudRequestFailedException(String message, Throwable innerException) {
        super(message, innerException);
    }
}
