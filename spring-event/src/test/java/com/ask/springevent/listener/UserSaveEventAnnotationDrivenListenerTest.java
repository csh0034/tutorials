package com.ask.springevent.listener;

import com.ask.springevent.entity.User;
import com.ask.springevent.event.UserSaveEvent;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class UserSaveEventAnnotationDrivenListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @DisplayName("Annotation-Driven 방식 + 비동기 처리 + Transaction")
  @Test
  void userSaveEvent() throws Exception {
    // given
    CountDownLatch countDownLatch = new CountDownLatch(2);

    User user = User.create("ask");
    UserSaveEvent event = new UserSaveEvent(user, "UserSaveEventAnnotationDrivenListenerTest!!!", countDownLatch);

    // when
    publisher.publishEvent(event);

    // then
    countDownLatch.await();
  }

}