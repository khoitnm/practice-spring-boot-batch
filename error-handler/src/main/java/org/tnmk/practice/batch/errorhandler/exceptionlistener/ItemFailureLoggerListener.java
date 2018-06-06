package org.tnmk.practice.batch.errorhandler.exceptionlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;

import java.util.List;

public class ItemFailureLoggerListener<I, O> extends ItemListenerSupport<I, O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemFailureLoggerListener.class);

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("Encountered error on read", ex);
    }

    @Override
    public void onWriteError(Exception ex, List<? extends O> item) {
        LOGGER.error("Encountered error on write", ex);
    }
}