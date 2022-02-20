package com.ask.openfeign.client;

import com.ask.openfeign.config.client.FacebookConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "facebook", url = "${feign.custom.facebook-url}", fallbackFactory = FacebookFallbackFactory.class, configuration = FacebookConfig.class)
public interface FacebookClient {

  @GetMapping("/notFound")
  String notFound();

}
