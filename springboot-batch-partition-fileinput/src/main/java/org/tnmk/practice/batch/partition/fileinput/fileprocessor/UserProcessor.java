package org.tnmk.practice.batch.partition.fileinput.fileprocessor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.tnmk.practice.batch.partition.fileinput.consts.JobParams;
import org.tnmk.practice.batch.partition.fileinput.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "step")
public class UserProcessor implements ItemProcessor<User, User> {

    @Value("#{jobParameters['" + JobParams.PARAM_JOB_INSTANCE_ID + "']}")
    private String jobInstanceId;

    private String processorName;

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    @Override
    public User process(User item) {
        System.out.println("JobInstanceId: " + jobInstanceId + ",\tThread: " + Thread.currentThread().getName() + ",\tProcessorName: " + processorName + ":\t{" + item.getId() + ", " + item.getUsername()+"}");
        return item;
    }
}
