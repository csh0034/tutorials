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
class SampleEventAnnotationDrivenListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @SpyBean
  private SampleEventAnnotationDrivenListener sampleEventAnnotationDrivenListener;

  @DisplayName("Annotation-Driven 방식")
  @Test
  void sampleEvent() {
    // given
    SampleEvent event = new SampleEvent("source!!!", "SampleEventAnnotationDrivenListenerTest!!!");

    // when
    publisher.publishEvent(event);

    // then
    then(sampleEventAnnotationDrivenListener).should(times(1)).handleSampleEvent(any());
  }

}