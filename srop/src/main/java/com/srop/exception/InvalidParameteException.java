package com.srop.exception;

/**
 * Created by xyyz150 on 2016/1/6.
 */
public class InvalidParameteException extends RuntimeException {
    public InvalidParameteException() {
    }

    public InvalidParameteException(String message) {
        super(message);
    }

    public InvalidParameteException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameteException(Throwable cause) {
        super(cause);
    }
}
