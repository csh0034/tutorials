package com.ask.springactuator.service;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * RequestContextFilter 찾아보다 궁금해서 갑자기 만듬..
 * Actuator 와 관계없음
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncService {

  private final HttpServletRequest request;

  @Async
  public void asyncMethod() {
//    Thread.sleep 하면 request 값이 null 나옴.... 왜그런지 모르겠는데 이것때문에 4시간 삽질함
//    try {
//      log.info("sleep 2s...");
//      Thread.sleep(2000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
    log.info("AsyncService request uri : {}", request.getRequestURI());
  }
}
