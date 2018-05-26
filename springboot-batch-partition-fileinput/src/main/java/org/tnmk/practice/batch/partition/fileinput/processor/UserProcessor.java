package org.tnmk.practice.batch.partition.fileinput.processor;

import org.tnmk.practice.batch.partition.fileinput.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserProcessor implements ItemProcessor<User, User> {

  private String threadName;

  public String getThreadName() {
    return threadName;
  }

  public void setThreadName(String threadName) {
    this.threadName = threadName;
  }

  @Override
  public User process(User item) throws Exception {
    System.out.println(threadName + " processing : "
        + item.getId() + " : " + item.getUsername());
    return item;
  }
}
