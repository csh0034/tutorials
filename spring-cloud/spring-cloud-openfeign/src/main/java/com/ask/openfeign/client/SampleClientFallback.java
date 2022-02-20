package com.ask.openfeign.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleClientFallback implements SampleClient {

  public static final String INDEX_NO_FALLBACK_MESSAGE = "index, No fallback available.";

  @Override
  public String index() {
    log.debug("sample index fallback");
    return INDEX_NO_FALLBACK_MESSAGE;
  }

}
