package com.ask.springtestcore.service;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

  public String formatParam(String param) {
    return "param : " + param;
  }

  public long unixTimestamp() {
    return System.currentTimeMillis();
  }

}
