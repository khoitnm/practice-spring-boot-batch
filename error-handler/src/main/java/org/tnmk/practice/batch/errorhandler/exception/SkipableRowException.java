package org.tnmk.practice.batch.errorhandler.exception;

public class SkipableRowException extends RuntimeException {

    public SkipableRowException(String message, Throwable cause) {
        super(message, cause);
    }
}
