package org.tnmk.practice.batch.simplesteps.concurrencyjobs.exception;

public class BatchJobException extends RuntimeException {
    public BatchJobException(Throwable e){
        super(e);
    }
}
