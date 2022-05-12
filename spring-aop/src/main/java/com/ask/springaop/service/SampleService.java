package com.ask.springaop.service;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

  public String sleep(long sleep) {
    try {
      Thread.sleep(sleep);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return "sleep: " + sleep;
  }

}
