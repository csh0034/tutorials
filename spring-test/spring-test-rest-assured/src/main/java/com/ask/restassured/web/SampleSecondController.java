package com.ask.restassured.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SampleSecondController {

  @GetMapping("/sample-second/today")
  public String today() {
    log.info("invoke today");
    return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

}
