package com.ask.springtestcore;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sleep {

  private static final int SLEEP_SECOND = 1;

  public Sleep() {
    try {
      log.info("Start Sleep for {} seconds when starting...", SLEEP_SECOND);
      TimeUnit.SECONDS.sleep(SLEEP_SECOND);
    } catch (InterruptedException e) {
      log.error("InterruptedException", e);
    }
  }
}
