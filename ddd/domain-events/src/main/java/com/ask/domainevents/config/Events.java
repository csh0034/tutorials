package com.ask.domainevents.config;

import org.springframework.context.ApplicationEventPublisher;

public class Events {

  private static ApplicationEventPublisher publisher;

  static void setPublisher(ApplicationEventPublisher publisher) {
    Events.publisher = publisher;
  }

  public static void publish(Object event) {
    if (publisher != null) {
      publisher.publishEvent(event);
    }
  }

}
