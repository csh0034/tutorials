package com.ask.eurekaclient.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @Value("${server.port}")
  private String port;

  @RequestMapping("/")
  public String home() {
    return "Hello world!! port : " + port;
  }
}
