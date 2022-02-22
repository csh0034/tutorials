package com.ask.springcore.lifecycle;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * 자동 start 호출, isRunning 일경우 서버 내려갈때 stop 호출
 */
@Component
@Slf4j
public class SampleSmartLifeCycle implements SmartLifecycle {

  private final AtomicBoolean running = new AtomicBoolean();

  @Override
  public void start() {
    log.info("start");
    running.set(true);
  }

  @Override
  public void stop() {
    log.info("stop");
  }

  @Override
  public boolean isRunning() {
    return running.get();
  }

}
