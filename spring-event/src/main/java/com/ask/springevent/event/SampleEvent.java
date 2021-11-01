package com.ask.springevent.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SampleEvent extends ApplicationEvent {

  private static final long serialVersionUID = 7160735850460474302L;

  private final String message;

  public SampleEvent(Object source, String message) {
    super(source);
    this.message = message;
  }

  @Override
  public String toString() {
    return "SampleEvent{" +
        "message='" + message + '\'' +
        ", source=" + source +
        '}';
  }
}
