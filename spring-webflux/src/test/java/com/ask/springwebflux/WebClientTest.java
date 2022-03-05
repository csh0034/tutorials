package com.ask.springwebflux;

import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Slf4j
public class WebClientTest {

  @Autowired
  private WebClient webClient;

  @Test
  public void nonBlocking() throws InterruptedException {
    int requestCount = 100;

    CountDownLatch count = new CountDownLatch(requestCount);

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    for (int i = 0; i < requestCount; i++) {
      webClient.get()
          .uri("/test")
          .retrieve()
          .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> clientResponse
              .bodyToMono(String.class).map(RuntimeException::new))
          .bodyToMono(String.class)
          .subscribe(result -> {
            count.countDown();
            log.info("result : {}", result);
          });
    }

    count.await();
    stopWatch.stop();

    log.info("TotalTimeSeconds : {}", stopWatch.getTotalTimeSeconds());
  }

}
