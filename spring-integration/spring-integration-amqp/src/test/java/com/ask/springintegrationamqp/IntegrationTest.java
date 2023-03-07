package com.ask.springintegrationamqp;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.integration.test.mock.MockMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Disabled
@SpringBootTest
@SpringIntegrationTest(noAutoStartup = "sampleFlowAdaptor")
class IntegrationTest {

  @Autowired
  private MockIntegrationContext mockIntegrationContext;

  @Resource(name = "bridgeChannel")
  private DirectChannel directChannel;

  @Test
  void test() {
    assertThat(mockIntegrationContext).isNotNull();

    MockMessageHandler mockMessageHandler = MockIntegration.mockMessageHandler()
        .handleNextAndReply(m -> "stubbing.. " + m.getPayload().toString().toLowerCase());

    mockIntegrationContext.substituteMessageHandlerFor("sampleFlowTransformerConsumer", mockMessageHandler);

    Message<String> message = MessageBuilder.withPayload("HELLO")
        .setHeader("foo", "bar")
        .build();

    directChannel.send(message);
  }

  @AfterEach
  void tearDown() {
    mockIntegrationContext.resetBeans();
  }

}
