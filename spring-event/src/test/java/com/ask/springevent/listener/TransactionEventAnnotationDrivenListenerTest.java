package com.ask.springevent.listener;

import com.ask.springevent.service.UserService;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionEventAnnotationDrivenListenerTest {

  @Autowired
  private UserService userService;

  @DisplayName("TransactionalEventListener, 익셉션 발생 할 경우")
  @Test
  void rollback() throws Exception {
    Assertions.assertThatThrownBy(() -> userService.rollback()).isInstanceOf(RuntimeException.class);
    TimeUnit.SECONDS.sleep(1);
  }

  @DisplayName("TransactionalEventListener, 익셉션 발생 안할 경우")
  @Test
  void saveTestUser() throws Exception {
    userService.saveTestUser();
    TimeUnit.SECONDS.sleep(1);
  }

}