package com.ask.sleuth.web;

import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderPrintController {

  @GetMapping("/{path}")
  public Map<String, String> path(@PathVariable String path, @RequestHeader HttpHeaders headers) {
    Map<String, String> map = headers.toSingleValueMap();
    map.put("path", path);
    return map;
  }

}
