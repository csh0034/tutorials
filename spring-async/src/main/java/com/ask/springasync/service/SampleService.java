package com.ask.springasync.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

  private final AsyncService asyncService;

  public void run() {
    asyncService.printTimestampLog();
  }

}
