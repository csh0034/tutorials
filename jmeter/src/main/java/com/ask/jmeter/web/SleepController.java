package com.ask.jmeter.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SleepController {

  @GetMapping("sleep")
  public String sleep(@RequestParam(defaultValue = "0") Long second) {
    try {
      Thread.sleep(second * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String message = "sleep: " + second + "s";
    log.info(message);

    return message;
  }

}
