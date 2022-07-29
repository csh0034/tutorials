package com.ask.logback.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SampleController {

  @GetMapping("/sample")
  public String sample() {
    log.info("sample...");
    return "sample...";
  }

}
