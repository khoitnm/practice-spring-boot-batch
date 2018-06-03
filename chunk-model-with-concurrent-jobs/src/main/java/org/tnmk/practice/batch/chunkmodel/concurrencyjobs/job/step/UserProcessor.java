package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.step;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.tnmk.common.batch.joblauncher.JobLauncherHelper;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.model.User;

public class UserProcessor implements ItemProcessor<User, User> {

    @Value("#{jobParameters['" + JobLauncherHelper.PARAM_JOB_INSTANCE_ID + "']}")
    private String jobInstanceId;

    @Override
    public User process(User item) {
        System.out.println("JobInstanceId: " + jobInstanceId + ",\tThread: " + Thread.currentThread().getName() + "\t{" + item.getId() + ", " + item.getUsername() + "}");
        return item;
    }
}
