package com.ask.jmeter.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SleepController {

  @GetMapping("sleep")
  public String sleep(@RequestParam(defaultValue = "0") Long second) {
    try {
      Thread.sleep(second * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "sleep: " + second + "s";
  }

}
