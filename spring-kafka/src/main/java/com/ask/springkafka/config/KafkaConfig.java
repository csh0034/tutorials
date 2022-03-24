package com.ask.springkafka.config;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerProperties;
import org.springframework.kafka.support.KafkaHeaders;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaConfig {

  private static final String SAMPLE_TOPIC = "sample-topic";
  private static final String SAMPLE_GROUP_ID = "sample-group-id";

  private final ConsumerFactory<Object, Object> consumerFactory;
  private final KafkaTemplate<Object, Object> kafkaTemplate;

  @Bean
  public ApplicationRunner appRunner() {
    return args -> IntStream.rangeClosed(1, 4)
        .parallel()
        .forEach(i -> {
          try {
            TimeUnit.SECONDS.sleep(3);
            kafkaTemplate.send(SAMPLE_TOPIC, "key-" + i, "data-" + i);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });
  }

  @Bean
  public NewTopic sampleTopic() {
    return TopicBuilder.name(SAMPLE_TOPIC)
        .partitions(1)
        .replicas(1)
        .build();
  }

//  @KafkaListener(topics = SAMPLE_TOPIC, groupId = SAMPLE_GROUP_ID)
//  public void kafkaListener(String message) {
//    log.info("message: {}", message);
//  }

  @Bean
  public IntegrationFlow kafkaInboundFlow() {
    return IntegrationFlows.from(Kafka.inboundChannelAdapter(consumerFactory, consumerProperties()))
        .handle(message -> {
          String receivedMessageKey = (String) message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY);
          String payload = (String) message.getPayload();
          log.info("receivedMessageKey: {}, payload: {}", receivedMessageKey, payload);
        })
        .get();
  }

  private ConsumerProperties consumerProperties() {
    ConsumerProperties consumerProperties = new ConsumerProperties(SAMPLE_TOPIC);
    consumerProperties.setGroupId(SAMPLE_GROUP_ID);
    return consumerProperties;
  }

}
