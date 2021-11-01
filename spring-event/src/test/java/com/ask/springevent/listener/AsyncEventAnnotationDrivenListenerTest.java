package com.ask.springevent.listener;

import com.ask.springevent.event.AsyncEvent;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class AsyncEventAnnotationDrivenListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @DisplayName("Annotation-Driven 방식 + 비동기 처리")
  @Test
  void asyncEvent() throws Exception {
    // given
    CountDownLatch countDownLatch = new CountDownLatch(1);
    AsyncEvent event = new AsyncEvent("source!!!", "AsyncEventAnnotationDrivenListenerTest!!!", countDownLatch);

    // when
    publisher.publishEvent(event);

    // then
    countDownLatch.await();
  }

}