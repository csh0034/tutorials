package com.ask.springintegrationamqp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.integration.test.mock.MockMessageHandler;

@Disabled
@SpringBootTest
@SpringIntegrationTest(noAutoStartup = "sampleFlow.amqp:inbound-channel-adapter#0")
class IntegrationTest {

  @Autowired
  private MockIntegrationContext mockIntegrationContext;

  @Test
  void test() {
    assertThat(mockIntegrationContext).isNotNull();

    MockMessageHandler mockMessageHandler = MockIntegration.mockMessageHandler()
        .handleNextAndReply(m -> m.getPayload().toString().toLowerCase());
    mockIntegrationContext.substituteMessageHandlerFor("sampleFlow.org.springframework.integration.config.ConsumerEndpointFactoryBean#0", mockMessageHandler);
  }

}
