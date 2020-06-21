package com.github.opf.exception;

/**
 * Created by xyyz150
 */
public class OpfException extends RuntimeException {
    public OpfException() {
    }

    public OpfException(String message) {
        super(message);
    }

    public OpfException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpfException(Throwable cause) {
        super(cause);
    }
}
