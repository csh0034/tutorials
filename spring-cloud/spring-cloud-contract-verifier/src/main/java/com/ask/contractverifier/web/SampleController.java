package com.ask.contractverifier.web;

import com.ask.contractverifier.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

  private final SampleService sampleService;

  @GetMapping("/name")
  public String name() {
    return sampleService.getName();
  }

}
