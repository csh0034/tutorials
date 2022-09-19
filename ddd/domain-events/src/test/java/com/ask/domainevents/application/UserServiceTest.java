package com.ask.domainevents.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import com.ask.domainevents.domain.event.DomainEvent;
import com.ask.domainevents.domain.event.DomainEventListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.repository.core.support.EventPublishingRepositoryProxyPostProcessor;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @SpyBean
  private DomainEventListener domainEventListener;

  @DisplayName("Service 에서 event 발행")
  @Test
  void saveWithEvent() {
    // given
    String name = "user name...";

    // when
    userService.saveWithEvent(name);

    // then
    then(domainEventListener).should().handle(any(DomainEvent.class));
  }

  @DisplayName("Domain 에서 static method 호출하여 event 발행")
  @Test
  void saveWithEvent2() {
    // given
    String name = "user name...";

    // when
    userService.saveWithEvent2(name);

    // then
    then(domainEventListener).should().handle(any(DomainEvent.class));
  }

  /**
   * @see EventPublishingRepositoryProxyPostProcessor
   */
  @DisplayName("@DomainEvents 를 사용하여 event 발행")
  @Test
  void saveWithEvent3() {
    // given
    String name = "user name...";

    // when
    userService.saveWithEvent3(name);

    // then
    then(domainEventListener).should().handle(any(DomainEvent.class));
  }

  /**
   * @see AbstractAggregateRoot
   */
  @DisplayName("AbstractAggregateRoot 를 사용하여 event 발행")
  @Test
  void saveWithEvent4() {
    // given
    String name = "user name...";

    // when
    userService.saveWithEvent4(name);

    // then
    then(domainEventListener).should().handle(any(DomainEvent.class));
  }

}
