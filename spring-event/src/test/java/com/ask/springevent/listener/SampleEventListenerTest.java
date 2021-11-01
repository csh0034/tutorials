package com.ask.springevent.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ask.springevent.event.SampleEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class SampleEventListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @SpyBean
  private SampleEventListener sampleEventListener;

  @DisplayName("ApplicationListener 구현 방식")
  @Test
  void sampleEvent() {
    // given
    SampleEvent event = new SampleEvent("source!!!", "SampleEventListenerTest!!!");

    // when
    publisher.publishEvent(event);

    // then
    then(sampleEventListener).should(times(1)).onApplicationEvent(any());
  }

}