package com.ask.rabbitmq.consumer.config;

import static com.ask.rabbitmq.consumer.config.MqttConfig.BROKER_URL;
import static com.ask.rabbitmq.consumer.config.MqttConfig.MQTT_OUTBOUND_CHANNEL;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttIntegrationUtils {

  private final IntegrationFlowContext integrationFlowContext;
  private final MqttPahoClientFactory mqttClientFactory;

  public static final String MQTT_DYNAMIC_TOPIC_1 = "/TOPIC1";
  public static final String MQTT_DYNAMIC_TOPIC_2 = "/TOPIC2";

  public static final String MQTT_OUTBOUND_DYNAMIC_TOPIC = "MQTT_OUTBOUND_DYNAMIC_TOPIC";

  /**
   * 동적 MqttInboundFlow 생성
   */
  public void createMqttInboundFlow() {
    IntegrationFlow dynamicIntegrationFlow1 = IntegrationFlows
        .from(mqttChannelAdapter(BROKER_URL, MQTT_DYNAMIC_TOPIC_1, mqttClientFactory))
        .handle(message -> log.info(message.toString()))
        .get();
    integrationFlowContext.registration(dynamicIntegrationFlow1).id(MQTT_DYNAMIC_TOPIC_1).register();

    IntegrationFlow dynamicIntegrationFlow2 = IntegrationFlows
        .from(mqttChannelAdapter(BROKER_URL, MQTT_DYNAMIC_TOPIC_2, mqttClientFactory))
        .handle(message -> log.info(message.toString()))
        .get();
    integrationFlowContext.registration(dynamicIntegrationFlow2).id(MQTT_DYNAMIC_TOPIC_2).register();

    log.info("key set : {}", integrationFlowContext.getRegistry().keySet());
  }

  public void removeMqttInboundFlow() {
    if (integrationFlowContext.getRegistrationById(MQTT_DYNAMIC_TOPIC_1) != null) {
      integrationFlowContext.remove(MQTT_DYNAMIC_TOPIC_1);
    } else {
      log.info(MQTT_DYNAMIC_TOPIC_1 + " is not registered");
    }

    if (integrationFlowContext.getRegistrationById(MQTT_DYNAMIC_TOPIC_2) != null) {
      integrationFlowContext.remove(MQTT_DYNAMIC_TOPIC_2);
    } else {
      log.info(MQTT_DYNAMIC_TOPIC_2 + " is not registered");
    }
  }

  public Set<String> registryKeySet() {
    return integrationFlowContext.getRegistry().keySet();
  }

  /**
   * 동적 MqttOutboundFlow 생성
   */
  public void createMqttOutboundFlow() {
    IntegrationFlow dynamicOutboundIntegrationFlow = IntegrationFlows
        .from(MQTT_OUTBOUND_CHANNEL)
        .handle(mqttOutboundMessageHandler(BROKER_URL, mqttClientFactory))
        .get();

    integrationFlowContext.registration(dynamicOutboundIntegrationFlow).id(MQTT_OUTBOUND_DYNAMIC_TOPIC).register();
  }

  public void removeMqttOutboundFlow() {
    if (integrationFlowContext.getRegistrationById(MQTT_OUTBOUND_DYNAMIC_TOPIC) != null) {
      integrationFlowContext.remove(MQTT_OUTBOUND_DYNAMIC_TOPIC);
    } else {
      log.info(MQTT_OUTBOUND_DYNAMIC_TOPIC + " is not registered");
    }
  }

  public static MqttPahoMessageDrivenChannelAdapter mqttChannelAdapter(String url, String topic, MqttPahoClientFactory mqttClientFactory) {
    MqttPahoMessageDrivenChannelAdapter adapter =
        new MqttPahoMessageDrivenChannelAdapter(url, MqttAsyncClient.generateClientId(), mqttClientFactory, topic);
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    return adapter;
  }

  public static MessageHandler mqttOutboundMessageHandler(String url, MqttPahoClientFactory mqttClientFactory) {
    MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(url, MqttAsyncClient.generateClientId(), mqttClientFactory);
    messageHandler.setAsync(true);
    messageHandler.setDefaultQos(1);
    return messageHandler;
  }
}
