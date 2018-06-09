package org.tnmk.practice.batch.errorhandler.exception;

/**
 * @deprecated Not used now
 */
@Deprecated
public class BatchAbortException extends RuntimeException {
    public BatchAbortException(String message, Throwable cause) {
        super(message, cause);
    }
}
