package com.ask.openfeign.client;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleClientFallback implements SampleClient {

  public static final String INDEX_NO_FALLBACK_MESSAGE = "index, No fallback available.";
  public static final String TIME_NO_FALLBACK_MESSAGE = "time, No fallback available.";

  @Override
  public String index() {
    log.debug("sample index fallback");
    return INDEX_NO_FALLBACK_MESSAGE;
  }

  @Override
  public String time(LocalDateTime startDt) {
    return TIME_NO_FALLBACK_MESSAGE;
  }

}
