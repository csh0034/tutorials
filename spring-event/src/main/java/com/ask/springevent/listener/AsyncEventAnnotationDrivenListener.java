package com.ask.springevent.listener;

import com.ask.springevent.event.AsyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncEventAnnotationDrivenListener {

  @Async
  @EventListener
  public void handleAsyncEvent(AsyncEvent event) {
    log.info("invoke countDown()");
    event.getCountDownLatch().countDown();

    log.info("AsyncEvent : {}", event);
  }
}
