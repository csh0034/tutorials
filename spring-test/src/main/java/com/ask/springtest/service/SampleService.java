package com.ask.springtest.service;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

  public String formatParam(String param) {
    return "param : " + param;
  }
}
