package com.ask.springevent.listener;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
class SampleEventListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @Autowired
  private ApplicationEvents applicationEvents;

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
    assertThat(applicationEvents.stream(SampleEvent.class)).hasSize(1);
    then(sampleEventListener).should(times(1)).onApplicationEvent(any());
  }

}
