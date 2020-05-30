package com.srop.exception;

/**
 * Created by xyyz150 on 2016/1/5.
 */
public class SropException extends RuntimeException {
    public SropException() {
    }

    public SropException(String message) {
        super(message);
    }

    public SropException(String message, Throwable cause) {
        super(message, cause);
    }

    public SropException(Throwable cause) {
        super(cause);
    }
}
