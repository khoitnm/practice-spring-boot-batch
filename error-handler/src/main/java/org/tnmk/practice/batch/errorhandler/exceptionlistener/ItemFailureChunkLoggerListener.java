package org.tnmk.practice.batch.errorhandler.exceptionlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.skip.NonSkippableProcessException;
import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.batch.core.step.skip.NonSkippableWriteException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.validation.FieldError;
import org.tnmk.common.batch.errorhandler.FlatFileParseExceptionUtils;
import org.tnmk.practice.batch.errorhandler.model.User;

import java.util.List;

public class ItemFailureChunkLoggerListener implements ChunkListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemFailureChunkLoggerListener.class);
    private static int ERROR_INDEX = 0;

    @Override
    public void beforeChunk(ChunkContext context) {
        LOGGER.error("before chunk: " + context);
    }

    @Override
    public void afterChunk(ChunkContext context) {
        LOGGER.error("after chunk: " + context);
    }

    /**
     * By default, the chunk which get the error records will be stopped. Other chunks which are running in parallel which keep going on until finished.
     * And the remain unprocessed records after that will not be processed by any further chunks anymore.
     * The step will be completely stopped after that.
     *
     * @param context
     */
    @Override
    public void afterChunkError(ChunkContext context) {
        String[] attributes = context.attributeNames();
        for (String attribute : attributes) {
            Object object = context.getAttribute(attribute);
            if (object instanceof Throwable) {
                writeLogFromException((Throwable) object);
            } else {
                LOGGER.debug("The Chunk attribute is not an exception: {{}:{}}", attribute, object);
            }
        }
        LOGGER.error("ERROR IN CHUNK: error chunk index conclusion: " + ERROR_INDEX + "_" + context);
        ERROR_INDEX++;
    }

    private void writeLogFromException(Throwable exception) {
        //If register the faultTolerant() in the fanOutStep, the first level Exception will be NonSkippableXXXException
        //Otherwise, the first level Exception will be FlatFileParseException or ProcessorException, or WriterException.
        if (exception instanceof NonSkippableReadException || exception instanceof NonSkippableProcessException || exception instanceof NonSkippableWriteException) {
            Throwable cause = exception.getCause();
            LOGGER.error("NonSkippableException: " + exception.getMessage());
            writeLogFromException(cause);
        } else if (exception instanceof FlatFileParseException) {
            writeLogForFlatFileParseException((FlatFileParseException) exception);
        } else {
            LOGGER.error("UNEXPECTED ERROR: error chunk exception: " + exception.getMessage(), exception);
        }
    }

    private void writeLogForFlatFileParseException(FlatFileParseException flatFileParseException) {
        Object errorTarget = FlatFileParseExceptionUtils.getErrorTarget(flatFileParseException);
        List<FieldError> fieldErrors = FlatFileParseExceptionUtils.getFieldErrors(flatFileParseException);
        writeErrorLog(errorTarget, fieldErrors, flatFileParseException);

        //Specific handler
        User errorUser = (User) errorTarget;
        if (errorUser.getId() == null) {
            LOGGER.error("FLAT FILE PARSE EXCEPTION: Cannot get id from the row, should stop the batch.", flatFileParseException);
//            throw new BatchAbortException("BATCH ERROR: Cannot get id from the row. Input data is wrong. Abort the whole batch.", flatFileParseException);
        } else {
            LOGGER.warn("FLAT FILE PARSE EXCEPTION: Can get id from the row, but some other fields in the row are corrupted, should skip it and collect error later.", flatFileParseException);
            //throw new SkipableRowException("SKIPABLE ROW: The row is error, skip it.", flatFileParseException);
        }
    }

    private void writeErrorLog(Object errorTarget, List<FieldError> fieldErrors, FlatFileParseException flatFileParseException) {
        StringBuilder fieldErrorsStringBuilder = new StringBuilder("Field Errors: ");
        for (FieldError fieldError : fieldErrors) {
            fieldErrorsStringBuilder.append("field name: ").append(fieldError.getField())
                .append("field value (error): ").append(fieldError.getRejectedValue());
        }
        LOGGER.error("error chunk[{}]:" +
                "\n\t input: {}" +
                "\n\t line number: {}" +
                "\n\t error target {}" +
                "\n\t error fields: {}" +
                "\n\t exception: {}",
            ERROR_INDEX,
            flatFileParseException.getInput(),
            flatFileParseException.getLineNumber(),
            errorTarget,
            fieldErrorsStringBuilder.toString(),
            flatFileParseException.getMessage());
    }
}
