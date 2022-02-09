package com.ask.integration.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.boot.ApplicationRunner;
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

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqttPubSubChannelConfig {

  public static final String MQTT_PUB_SUB_OUTBOUND_CHANNEL = "pubSubOutboundChannel";
  public static final String MQTT_PUB_SUB_TOPIC = "/PUBSUB";
  public static final String MQTT_SERVER_1 = "tcp://localhost:1883";
  public static final String MQTT_SERVER_2 = "tcp://localhost:1884";

  public static final String MQTT_HANDLER_1 = "handler-1";
  public static final String MQTT_HANDLER_2 = "handler-2";

  private final MqttPahoClientFactory mqttClientFactory;
  private final ObjectMapper objectMapper;

  @Bean
  public ApplicationRunner mqttSendRunner(MessageChannel pubSubOutboundChannel) {
    return args -> IntStream.rangeClosed(0, 4).parallel()
        .forEach(i -> {
          try {
            TimeUnit.SECONDS.sleep(5);

            String handlerId = MQTT_HANDLER_1;
            if (i % 2 == 0) {
              handlerId = MQTT_HANDLER_2;
            }

            Message<String> message = MessageBuilder
                .withPayload(objectMapper.writeValueAsString(new SampleMessage(i, "ask")))
                .setHeader(MqttHeaders.TOPIC, MQTT_PUB_SUB_TOPIC)
                .setHeader(MqttHeaders.QOS, 1)
                .setHeader(CustomMqttMultiMessageHandler.MQTT_HANDLER_ID, handlerId)
                .build();

            pubSubOutboundChannel.send(message);

          } catch (Exception e) {
            e.printStackTrace();
          }
        });
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
        .log("mqttPubSubOutboundFlow1")
        .handle(new CustomMqttMultiMessageHandler(mqttClientFactory))
        .get();
  }

//  위의 flow 와 중복
//  @Bean
//  public IntegrationFlow mqttPubSubOutboundFlow2() {
//    return IntegrationFlows
//        .from(mqttPubSubOutboundChannel())
//        .log("mqttPubSubOutboundFlow2")
//        .filter((MessageSelector) message -> true)
//        .handle(customMqttOutboundMessageHandler)
////        .handle(MqttIntegrationUtils.mqttOutboundMessageHandler(MQTT_SERVER_2, mqttClientFactory))
//        .get();
//  }

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
    void publish(@Header(CustomMqttMultiMessageHandler.MQTT_HANDLER_ID) String mqttHandlerId, @Header(MqttHeaders.TOPIC) String topic, String data);
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

  @RequiredArgsConstructor
  @Slf4j
  public static class CustomMqttMultiMessageHandler extends AbstractMessageHandler implements Lifecycle {

    public static final String MQTT_HANDLER_ID = MqttHeaders.PREFIX + "handlerId";

    private final AtomicBoolean running = new AtomicBoolean();
    private final Map<String, MessageHandler> mqttHandlerMap = new ConcurrentHashMap<>();

    private final MqttPahoClientFactory mqttClientFactory;

    @Override
    protected void handleMessageInternal(Message<?> message) {
      if (mqttHandlerMap.isEmpty()) {
        throw new RuntimeException("There is no registered handler");
      }

      String mqttHandlerId = message.getHeaders().get(MQTT_HANDLER_ID, String.class);
      CustomMqttPahoMessageHandler messageHandler = (CustomMqttPahoMessageHandler) mqttHandlerMap.get(mqttHandlerId);
      if (messageHandler == null) {
        throw new RuntimeException(String.format("There is no registered handler. id : %s", mqttHandlerId));
      }

      messageHandler.handleMessageInternal(message);
    }

    @Override
    public void start() {
      if (!running.getAndSet(true)) {
        doStart();
      }
    }

    private void doStart() {
      createDefaultHandlers();
      printCreationLog();
    }

    private void createDefaultHandlers() {
      mqttHandlerMap.put(MQTT_HANDLER_1, createHandler(MQTT_SERVER_1, mqttClientFactory));
      mqttHandlerMap.put(MQTT_HANDLER_2, createHandler(MQTT_SERVER_2, mqttClientFactory));
    }

    private MessageHandler createHandler(String url, MqttPahoClientFactory mqttClientFactory) {
      String clientId = MqttClient.generateClientId();

      CustomMqttPahoMessageHandler messageHandler = new CustomMqttPahoMessageHandler(url, clientId, mqttClientFactory);
      messageHandler.setDefaultQos(1);
      messageHandler.setAsync(true);
      messageHandler.setCompletionTimeout(5000);
      messageHandler.afterPropertiesSet();
      return messageHandler;
    }

    private void printCreationLog() {
      log.info("finish doStart, current handler count : {}", mqttHandlerMap.size());
      mqttHandlerMap.forEach((k, v) -> log.info("outboundHandlerId : {}", k));
    }

    @Override
    public void stop() {
      if (running.getAndSet(false)) {
        mqttHandlerMap.forEach((k, v) -> ((CustomMqttPahoMessageHandler)v).doStop());
      }
    }

    @Override
    public boolean isRunning() {
      return running.get();
    }
  }

  public static class CustomMqttPahoMessageHandler extends MqttPahoMessageHandler {

    public CustomMqttPahoMessageHandler(String brokerUrl, String clientId, MqttPahoClientFactory clientFactory) {
      super(brokerUrl, clientId, clientFactory);
    }

    @Override
    public void doStop() {
      super.doStop();
    }

    @Override
    public void handleMessageInternal(Message<?> message) {
      super.handleMessageInternal(message);
    }

    @Override
    public void onInit() {
      try {
        super.onInit();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
