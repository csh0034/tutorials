package com.ask.springactuator.web;

import com.ask.springactuator.service.AsyncService;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AsyncController {

  private final AsyncService asyncService;
  private final HttpServletRequest request;

  @GetMapping("/async")
  public String async() {
    CompletableFuture.runAsync(() -> log.info("AsyncController request uri : {}", request.getRequestURI()));
    asyncService.asyncMethod();
    return "Done";
  }
}
