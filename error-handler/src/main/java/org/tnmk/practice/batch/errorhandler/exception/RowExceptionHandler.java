package org.tnmk.practice.batch.errorhandler.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.tnmk.practice.batch.errorhandler.exceptionlistener.ItemFailureChunkLoggerListener;

public class RowExceptionHandler implements ExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RowExceptionHandler.class);

    /**
     * This method will be invoked after the {@link ItemFailureChunkLoggerListener#afterChunkError(ChunkContext)}
     *
     * @param context
     * @param throwable
     * @throws Throwable
     */
    @Override
    public void handleException(RepeatContext context, Throwable throwable) throws Throwable {
        
        LOGGER.info(context.toString());
    }
}
