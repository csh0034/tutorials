package com.ask.awsmessagingsqs;

import com.ask.awsmessagingsqs.config.SqsConstants;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.core.TopicMessageChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootTest
class QueueMessagingTemplateTest {

  @Autowired
  private QueueMessagingTemplate queueMessagingTemplate;

  @Test
  void sendStandardQueue() {
    queueMessagingTemplate.convertAndSend(SqsConstants.STANDARD_QUEUE, "message..");
  }

  @Test
  void sendFifoQueue() {
    Message<String> message = MessageBuilder.withPayload("message...")
        .setHeader(TopicMessageChannel.MESSAGE_GROUP_ID_HEADER, "fifo-group-1")
        .build();
    queueMessagingTemplate.send(SqsConstants.FIFO_QUEUE, message);
  }

}
