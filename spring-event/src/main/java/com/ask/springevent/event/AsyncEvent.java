package com.ask.springevent.event;

import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AsyncEvent extends ApplicationEvent {

  private static final long serialVersionUID = 7160735850460474302L;

  private final String message;
  private final CountDownLatch countDownLatch;

  public AsyncEvent(Object source, String message, CountDownLatch countDownLatch) {
    super(source);
    this.message = message;
    this.countDownLatch = countDownLatch;
  }

  @Override
  public String toString() {
    return "AsyncEvent{" +
        "message='" + message + '\'' +
        ", source=" + source +
        '}';
  }
}
