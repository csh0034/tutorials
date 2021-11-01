package com.ask.springevent.listener;

import com.ask.springevent.event.SampleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleEventAnnotationDrivenListener {

  @EventListener
  public void handleSampleEvent(SampleEvent event) {
    log.info("SampleEvent : {}", event);
  }
}
