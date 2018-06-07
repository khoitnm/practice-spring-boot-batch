package org.tnmk.practice.batch.errorhandler.exception;

public class BatchAbortException extends RuntimeException {
    public BatchAbortException(String message, Throwable cause) {
        super(message, cause);
    }
}
