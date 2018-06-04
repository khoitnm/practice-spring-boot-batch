package org.tnmk.practice.batch.faninstep.job.step;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.tnmk.common.batch.jobparam.JobParams;
import org.tnmk.practice.batch.faninstep.model.User;

/**
 * This is the processor of fan-out step.
 */
public class UserProcessor implements ItemProcessor<User, User> {

    @Value("#{jobParameters['" + JobParams.PARAM_JOB_INSTANCE_ID + "']}")
    private String jobInstanceId;

    @Override
    public User process(User item) {
        System.out.println("JobInstanceId: " + jobInstanceId + ",\tThread: " + Thread.currentThread().getName() + "\t{" + item.getId() + ", " + item.getUsername() + "}");
        return item;
    }
}
