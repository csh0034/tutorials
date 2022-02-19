package com.ask.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "sample", url = "http://localhost:${server.port}", fallback = SampleClientFallback.class)
public interface SampleClient {

  @GetMapping("/")
  String index();

}
