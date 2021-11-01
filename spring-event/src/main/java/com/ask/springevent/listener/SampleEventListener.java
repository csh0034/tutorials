package com.ask.springevent.listener;

import com.ask.springevent.event.SampleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleEventListener implements ApplicationListener<SampleEvent> {

  @Override
  public void onApplicationEvent(SampleEvent event) {
    log.info("SampleEvent : {}", event);
  }
}
