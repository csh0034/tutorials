package com.ask.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google", url = "${feign.custom.google-url}")
public interface GoogleClient {

  @GetMapping("/")
  String index();

  @GetMapping("/search")
  String search(@RequestParam(name = "q") String word);

}
