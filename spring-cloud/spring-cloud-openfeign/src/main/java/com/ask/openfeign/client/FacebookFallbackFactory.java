package com.ask.openfeign.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FacebookFallbackFactory implements FallbackFactory<FacebookClient> {

  private final FacebookClientFallback facebookClientFallback;

  @Override
  public FacebookClient create(Throwable cause) {
    log.debug("[Facebook] error occurred", cause);
    return facebookClientFallback;
  }

}
