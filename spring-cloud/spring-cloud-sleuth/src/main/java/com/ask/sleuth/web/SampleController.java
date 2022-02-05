package com.ask.sleuth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

  private final RestTemplate restTemplate;

  @GetMapping
  public String index() {
    ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:9999/sample/2", String.class);
    return String.format("restTemplate result : %s", result.getBody());
  }

  @GetMapping("/2")
  public String sample() {
    return "sample2";
  }
}
