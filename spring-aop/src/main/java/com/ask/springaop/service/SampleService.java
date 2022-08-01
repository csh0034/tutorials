package com.ask.springaop.service;

import com.ask.springaop.common.GenericService;
import org.springframework.stereotype.Service;

@Service
public class SampleService extends GenericService {

  public String sleep(long sleep) {
    try {
      Thread.sleep(sleep);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return "sleep: " + sleep;
  }

}
