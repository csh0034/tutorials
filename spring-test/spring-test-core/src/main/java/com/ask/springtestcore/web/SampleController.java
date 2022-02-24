package com.ask.springtestcore.web;

import com.ask.springtestcore.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

  private final SampleService sampleService;

  @GetMapping("/")
  public String formatParam(String param) {
    return sampleService.formatParam(param);
  }
}
