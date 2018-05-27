package org.tnmk.practice.batch.partition.fileinput.fileprocessor;

import org.tnmk.practice.batch.partition.fileinput.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserProcessor implements ItemProcessor<User, User> {

    private String processorName;

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    @Override
    public User process(User item) {
        System.out.println("Thread " + Thread.currentThread().getName() + "\t" + processorName + " processing : " + item.getId() + " : " + item.getUsername());
        return item;
    }
}
