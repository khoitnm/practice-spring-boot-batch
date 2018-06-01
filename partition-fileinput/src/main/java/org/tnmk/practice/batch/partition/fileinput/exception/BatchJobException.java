package org.tnmk.practice.batch.partition.fileinput.exception;

public class BatchJobException extends RuntimeException {
    public BatchJobException(Throwable e){
        super(e);
    }
}
