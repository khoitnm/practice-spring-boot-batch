package com.walking.techie.processor;

import com.walking.techie.model.User;
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
