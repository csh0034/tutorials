package com.ask.openfeign.client;

import java.time.LocalDateTime;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sample", url = "${feign.custom.sample-url}", fallback = SampleClientFallback.class)
public interface SampleClient {

  @GetMapping("/")
  String index();

  @GetMapping("/time")
  String time(@RequestParam LocalDateTime startDt);

}
