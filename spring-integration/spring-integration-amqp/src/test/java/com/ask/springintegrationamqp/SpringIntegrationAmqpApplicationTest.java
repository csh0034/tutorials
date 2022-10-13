package com.ask.springintegrationamqp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.dsl.context.IntegrationFlowContext;

@SpringBootTest
@Slf4j
class SpringIntegrationAmqpApplicationTest {

  @Autowired
  private IntegrationFlowContext integrationFlowContext;

  @Autowired
  private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

  @Autowired
  private AmqpAdmin amqpAdmin;

  @Autowired
  private RabbitTemplate rabbitTemplate;


  @DisplayName("queue 와 integration flow 생성후 queue 만 삭제할 경우 integrationFlowContext 에 integration flow 가 남아 메모리릭 발생할수 있음")
  @Test
  void queue() {
    String queueName = "test-1";

    // 1. queue 생성
    Queue queue = QueueBuilder.durable(queueName).build();
    amqpAdmin.declareQueue(queue);

    // 2. dynamic integration flow 생성
    SimpleMessageListenerContainer listenerContainer = rabbitListenerContainerFactory.createListenerContainer();
    listenerContainer.setQueueNames(queueName);

    IntegrationFlow integrationFlow = IntegrationFlows.from(Amqp.inboundAdapter(listenerContainer))
        .transform(Transformers.objectToString())
        .handle(message -> log.info("test flow :" + message.getPayload()))
        .get();

    integrationFlowContext.registration(integrationFlow).id(queueName).register();

    // 3. amqp 메세지 전송
    rabbitTemplate.convertAndSend(queueName, "test...");

    // 4. queue 삭제
    amqpAdmin.deleteQueue(queueName);

    // 5. integrationFlowContext 에 integration flow 남아있는지 확인
    List<String> keys = new ArrayList<>(integrationFlowContext.getRegistry().keySet());
    assertThat(keys).containsExactly(queueName);

    // 6. integration flow 제거
    integrationFlowContext.remove(queueName);
  }

}
