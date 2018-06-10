package org.tnmk.practice.batch.errorhandler.job.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.tnmk.common.batch.jobparam.JobParams;
import org.tnmk.practice.batch.errorhandler.model.User;

/**
 * This is the processor of fan-out step.
 */
public class UserProcessor implements ItemProcessor<User, User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProcessor.class);
    @Value("#{jobParameters['" + JobParams.PARAM_JOB_INSTANCE_ID + "']}")
    private String jobInstanceId;

    @Override
    public User process(User item) {
        LOGGER.info("\nPROCESS ITEM: " + item
            + "\n\tJobInstanceId: " + jobInstanceId
            + "\n\tThread: " + Thread.currentThread().getName()
            + "\n\t{" + item.getId() + ", " + item.getUsername() + "}");
        return item;
    }
}
