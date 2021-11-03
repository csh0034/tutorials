package com.ask.integration.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqttPubSubChannelConfig {

  public static final String MQTT_PUB_SUB_OUTBOUND_CHANNEL = "pubSubOutboundChannel";
  public static final String MQTT_PUB_SUB_TOPIC = "/PUBSUB";
  public static final String MQTT_SERVER_1 = "tcp://localhost:1883";
  public static final String MQTT_SERVER_2 = "tcp://localhost:1884";

  public static final String MQTT_HANDLER_ID = MqttHeaders.PREFIX + "handlerId";
  public static final String MQTT_HANDLER_1 = "handler-1";
  public static final String MQTT_HANDLER_2 = "handler-2";

  private final CustomMqttOutboundMessageHandler customMqttOutboundMessageHandler;
  private final MqttPahoClientFactory mqttClientFactory;
  private final ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    for (int i = 0; i < 5; i++) {
      int finalI = i;

      CompletableFuture.runAsync(() -> {
          try {
            TimeUnit.SECONDS.sleep(5);

            String handlerId = MQTT_HANDLER_1;
            if (finalI % 2 == 0) {
              handlerId = MQTT_HANDLER_2;
            }

            Message<String> message = MessageBuilder
                .withPayload(objectMapper.writeValueAsString(new SampleMessage(finalI, "ask")))
                .setHeader(MqttHeaders.TOPIC, MQTT_PUB_SUB_TOPIC)
                .setHeader(MqttHeaders.QOS, 1)
                .setHeader(MQTT_HANDLER_ID, handlerId)
                .build();

            mqttPubSubOutboundChannel().send(message);

          } catch (Exception e) {
            e.printStackTrace();
          }
      });
    }
  }

  @Bean
  public IntegrationFlow mqttPubSubInboundFlow1() {
    return IntegrationFlows
        .from(MqttIntegrationUtils.mqttChannelAdapter(MQTT_SERVER_1, MQTT_PUB_SUB_TOPIC, mqttClientFactory))
        .transform(Transformers.fromJson(SampleMessage.class))  // 클래스 지정안하면 json 을 LinkedHashMap 로 변환함
        .handle(message -> log.info("mqttPubSubInboundFlow1 : {}", message.getPayload()))
        .get();
  }

  @Bean
  public IntegrationFlow mqttPubSubInboundFlow2() {
    return IntegrationFlows
        .from(MqttIntegrationUtils.mqttChannelAdapter(MQTT_SERVER_2, MQTT_PUB_SUB_TOPIC, mqttClientFactory))
        .transform(Transformers.fromJson(SampleMessage.class))
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
        //log.info("preSend");
        return message;
      }

      @Override
      public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        //log.info("postSend");
      }
    };
  }

  @Bean
  public IntegrationFlow mqttPubSubOutboundFlow1() {
    return IntegrationFlows
        .from(mqttPubSubOutboundChannel())
        .handle(customMqttOutboundMessageHandler)
        .get();
  }

  @Bean
  public IntegrationFlow mqttPubSubOutboundFlow2() {
    return IntegrationFlows
        .from(mqttPubSubOutboundChannel())
        .handle(customMqttOutboundMessageHandler)
//        .handle(MqttIntegrationUtils.mqttOutboundMessageHandler(MQTT_SERVER_2, mqttClientFactory))
        .get();
  }

//  @Bean
//  public IntegrationFlow subFlowTest1() {
//    return f -> f
//        .handle(MqttIntegrationUtils.mqttOutboundMessageHandler(MQTT_SERVER_1, mqttClientFactory));
//  }
//
//  @Bean
//  public IntegrationFlow subFlowTest2() {
//    return f -> f
//        .handle(MqttIntegrationUtils.mqttOutboundMessageHandler(MQTT_SERVER_2, mqttClientFactory));
//  }

  @MessagingGateway(defaultRequestChannel = MQTT_PUB_SUB_OUTBOUND_CHANNEL, defaultRequestTimeout = "5000", defaultReplyTimeout = "5000")
  public interface MqttPubSubOutboundGateway {

    @Gateway
    void publish(@Header(MQTT_HANDLER_ID) String mqttHandlerId, @Header(MqttHeaders.TOPIC) String topic, String data);
  }

  @Bean
  public IntegrationFlow errorHandlingFlow() {
    return IntegrationFlows
        .from("errorChannel")
        .handle(message -> log.error("error occurred!! : {}", message.getPayload()))
        .get();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SampleMessage {
    private int index;
    private String name;
  }

  @Component
  @RequiredArgsConstructor
  @Slf4j
  public static class CustomMqttOutboundMessageHandler  extends AbstractMessageHandler implements Lifecycle {

    private final AtomicBoolean running = new AtomicBoolean();

    private final MqttPahoClientFactory mqttClientFactory;
    private final Map<String, MessageHandler> mqttHandlerMap = new ConcurrentHashMap<>();

    @Override
    protected void handleMessageInternal(Message<?> message) {
      if (mqttHandlerMap.isEmpty()) {
        throw new IllegalStateException("등록된 핸들러가 없습니다.");
      }

      String mqttHandlerId = message.getHeaders().get(MQTT_HANDLER_ID, String.class);
      MessageHandler messageHandler = mqttHandlerMap.get(mqttHandlerId);
      if (messageHandler == null) {
        throw new IllegalArgumentException("등록된 핸들러가 없습니다. id : [" + mqttHandlerId + "]");
      }

      messageHandler.handleMessage(message);

      // 모든 핸들러에 메세지 전송
      //mqttHandlerMap.forEach((k, v) -> v.handleMessage(message));
    }

    @Override
    public void start() {
      if (running.getAndSet(true)) {
        return;
      }
      initializeDefaultHandlers();
    }

    public void initializeDefaultHandlers() {
      clearRegisteredHandlers();
      createDefaultHandlers();
      log.info("Invoke initializeDefaultHandlers, Current Handler Count : {}", mqttHandlerMap.size());
    }

    private void createDefaultHandlers() {
      mqttHandlerMap.put(MQTT_HANDLER_1, createHandler(MQTT_SERVER_1, mqttClientFactory));
      mqttHandlerMap.put(MQTT_HANDLER_2, createHandler(MQTT_SERVER_2, mqttClientFactory));
    }

    private MessageHandler createHandler(String url, MqttPahoClientFactory mqttClientFactory) {
      MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(url, MqttClient.generateClientId(), mqttClientFactory);
      messageHandler.setAsync(true);
      messageHandler.setDefaultQos(1);
      messageHandler.afterPropertiesSet();
      return messageHandler;
    }

    @Override
    public void stop() {
      // TODO stop 호출시에 내부적으로 running.getAndSet false 여서 처리가 안됨 상속받아서 doStop 호출하게 해야할듯
      mqttHandlerMap.forEach((k, v) -> ((MqttPahoMessageHandler)v).stop());
    }

    @Override
    public boolean isRunning() {
      return running.get();
    }

    public void clearRegisteredHandlers() {
      log.info("Invoke clearRegisteredHandlers");
      stop();
      mqttHandlerMap.clear();
    }
  }
}
