package com.ask.openfeign.client;

import org.springframework.stereotype.Component;

@Component
public class SampleClientFallback implements SampleClient {

  public static final String INDEX_NO_FALLBACK_MESSAGE = "index, No fallback available.";

  @Override
  public String index() {
    return INDEX_NO_FALLBACK_MESSAGE;
  }

}
