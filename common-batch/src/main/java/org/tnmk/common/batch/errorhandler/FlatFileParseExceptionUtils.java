package org.tnmk.common.batch.errorhandler;

import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class FlatFileParseExceptionUtils {

    public static BindingResult getBindingResult(FlatFileParseException flatFileParseException) {
        BindException bindException = (BindException) flatFileParseException.getCause();
        return bindException.getBindingResult();
    }

    public static Object getErrorTarget(FlatFileParseException flatFileParseException) {
        BindingResult bindingResult = getBindingResult(flatFileParseException);
        return bindingResult.getTarget();
    }

    public static List<FieldError> getFieldErrors(FlatFileParseException flatFileParseException) {
        BindingResult bindingResult = getBindingResult(flatFileParseException);
        return bindingResult.getFieldErrors();
    }
}
