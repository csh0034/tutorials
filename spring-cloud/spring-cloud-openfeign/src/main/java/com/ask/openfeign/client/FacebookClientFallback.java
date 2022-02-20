package com.ask.openfeign.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FacebookClientFallback implements FacebookClient {

  public static final String INDEX_FALLBACK_MESSAGE = "invoke facebook fallback";

  @Override
  public String notFound() {
    log.debug("facebook notFound fallback");
    return INDEX_FALLBACK_MESSAGE;
  }

}
