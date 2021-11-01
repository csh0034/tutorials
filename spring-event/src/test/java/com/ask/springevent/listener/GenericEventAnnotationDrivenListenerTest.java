package com.ask.springevent.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.ask.springevent.entity.User;
import com.ask.springevent.event.GenericEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class GenericEventAnnotationDrivenListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @SpyBean
  private GenericEventAnnotationDrivenListener genericEventAnnotationDrivenListener;

  @DisplayName("제네릭 이벤트, condition : true")
  @Test
  void genericEventWithConditionTrue() {
    // given
    User user = User.create("ask-true");
    GenericEvent<User> event = new GenericEvent<>(user, true);

    // when
    publisher.publishEvent(event);

    // then
    then(genericEventAnnotationDrivenListener).should(atLeastOnce()).handleGenericEvent(any());
  }

  @DisplayName("제네릭 이벤트, condition : false")
  @Test
  void genericEventWithConditionFalse() {
    // given
    User user = User.create("ask-false");
    GenericEvent<User> event = new GenericEvent<>(user, false);

    // when
    publisher.publishEvent(event);

    // then
    then(genericEventAnnotationDrivenListener).should(never()).handleGenericEvent(any());
  }
}