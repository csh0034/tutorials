package com.ask.sleuth.web;

import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

  @GetMapping("/jwt")
  public Map<String, String> sample(@RequestHeader HttpHeaders headers) {
    return headers.toSingleValueMap();
  }

}
