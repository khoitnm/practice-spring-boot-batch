package org.tnmk.practice.batch.errorhandler.exceptionlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.validation.FieldError;
import org.tnmk.common.batch.errorhandler.FlatFileParseExceptionUtils;
import org.tnmk.practice.batch.errorhandler.exception.BatchAbortException;
import org.tnmk.practice.batch.errorhandler.exception.SkipableRowException;
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

    @Override
    public void afterChunkError(ChunkContext context) {
        String[] attributes = context.attributeNames();
        for (String attribute : attributes) {
            Object object = context.getAttribute(attribute);
            if (object instanceof FlatFileParseException) {
                FlatFileParseException flatFileParseException = (FlatFileParseException) object;
                Object errorTarget = FlatFileParseExceptionUtils.getErrorTarget(flatFileParseException);
                List<FieldError> fieldErrors = FlatFileParseExceptionUtils.getFieldErrors(flatFileParseException);
                writeErrorLog(errorTarget, fieldErrors, flatFileParseException);

                //Specific handler
                User errorUser = (User) errorTarget;
                if (errorUser.getId() == null) {
                    throw new BatchAbortException("Cannot get id from the row. Input data is wrong. Abort the whole batch.", flatFileParseException);
                } else {
                    throw new SkipableRowException("The row is error, skip it.", flatFileParseException);
                }
            } else if (object instanceof Exception) {
                Exception exception = (Exception) object;
                LOGGER.error("error chunk exception: " + exception.getMessage());
            }
        }
        LOGGER.error("error chunk index conclusion: " + ERROR_INDEX + "_" + context);
        ERROR_INDEX++;
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
