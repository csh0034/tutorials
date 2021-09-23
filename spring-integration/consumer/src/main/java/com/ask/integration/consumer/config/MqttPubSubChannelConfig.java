package com.ask.integration.consumer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqttPubSubChannelConfig {

  public static final String MQTT_PUB_SUB_OUTBOUND_CHANNEL = "pubSubOutboundChannel";
  public static final String MQTT_PUB_SUB_TOPIC = "/PUBSUB";
  public static final String MQTT_SERVER_1 = "tcp://localhost:1883";
  public static final String MQTT_SERVER_2 = "tcp://localhost:1884";

  private final MqttPahoClientFactory mqttClientFactory;

  @Bean
  public IntegrationFlow mqttPubSubInboundFlow1() {
    return IntegrationFlows
        .from(MqttIntegrationUtils.mqttChannelAdapter(MQTT_SERVER_1, MQTT_PUB_SUB_TOPIC, mqttClientFactory))
        .handle(message -> log.info("mqttPubSubInboundFlow1 : {}", message.getPayload()))
        .get();
  }

  @Bean
  public IntegrationFlow mqttPubSubInboundFlow2() {
    return IntegrationFlows
        .from(MqttIntegrationUtils.mqttChannelAdapter(MQTT_SERVER_2, MQTT_PUB_SUB_TOPIC, mqttClientFactory))
        .handle(message -> log.info("mqttPubSubInboundFlow2 : {}", message.getPayload()))
        .get();
  }

  @Bean(name = MQTT_PUB_SUB_OUTBOUND_CHANNEL)
  public MessageChannel mqttPubSubOutboundChannel() {
    return MessageChannels.publishSubscribe()
        .interceptor(channelInterceptor())
        .get();
  }

  @Bean
  public ChannelInterceptor channelInterceptor() {
    return new ChannelInterceptor() {
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // null 을 리턴하면 실제 전송 호출이 발생하지 않는다.
        log.info("preSend");
        return message;
      }

      @Override
      public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("postSend");
      }
    };
  }

  @Bean
  public IntegrationFlow mqttPubSubOutboundFlow1() {
    return IntegrationFlows
        .from(mqttPubSubOutboundChannel())
        .handle(MqttIntegrationUtils.mqttOutboundMessageHandler(MQTT_SERVER_1, mqttClientFactory))
        .get();
  }

  @Bean
  public IntegrationFlow mqttPubSubOutboundFlow2() {
    return IntegrationFlows
        .from(mqttPubSubOutboundChannel())
        .handle(MqttIntegrationUtils.mqttOutboundMessageHandler(MQTT_SERVER_2, mqttClientFactory))
        .get();
  }

  @MessagingGateway(defaultRequestChannel = MQTT_PUB_SUB_OUTBOUND_CHANNEL, defaultRequestTimeout = "5000", defaultReplyTimeout = "5000")
  public interface MqttPubSubOutboundGateway {

    @Gateway
    void publish(@Header(MqttHeaders.TOPIC) String topic, String data);
  }
}
