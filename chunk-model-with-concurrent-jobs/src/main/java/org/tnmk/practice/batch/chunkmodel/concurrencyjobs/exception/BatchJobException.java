package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.exception;

public class BatchJobException extends RuntimeException {
    public BatchJobException(Throwable e){
        super(e);
    }
}
