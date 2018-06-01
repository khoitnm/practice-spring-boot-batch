package org.tnmk.practice.batch.simplesteps.concurrencyjobs.fileprocessor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.tnmk.practice.batch.simplesteps.concurrencyjobs.consts.JobParams;
import org.tnmk.practice.batch.simplesteps.concurrencyjobs.consts.PartitionContextParams;
import org.tnmk.practice.batch.simplesteps.concurrencyjobs.model.User;


public class UserProcessor implements ItemProcessor<User, User> {

    @Value("#{jobParameters['" + JobParams.PARAM_JOB_INSTANCE_ID + "']}")
    private String jobInstanceId;

    @Value("#{stepExecutionContext[" + PartitionContextParams.FROM_INDEX + "]}")
    private String processorName;

    @Override
    public User process(User item) {
        System.out.println("JobInstanceId: " + jobInstanceId + ",\tThread: " + Thread.currentThread().getName() + ",\tProcessorName: " + processorName + ":\t{" + item.getId() + ", " + item.getUsername()+"}");
        return item;
    }
}
