package com.ask.domainevents.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsConfig {

  @Bean
  public InitializingBean eventsInitializer(ApplicationEventPublisher eventPublisher) {
    return () -> Events.setPublisher(eventPublisher);
  }

}
