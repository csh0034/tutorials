package com.ask.springartemis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SpringArtemisApplicationTest {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Test
  void send() {
    jmsTemplate.convertAndSend("testQueue", "anycast message");
  }

  @TestConfiguration
  @Slf4j
  public static class ArtemisConfig {

    @JmsListener(destination = "testQueue")
    public void testQueue(String content) {
      log.info("testQueue: {}", content);
    }

  }

}
