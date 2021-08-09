package com.ask.rabbitmq.consumer.config;

import static com.ask.rabbitmq.consumer.config.MqttConfig.BROKER_URL;

import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DynamicIntegrationUtils {

  private final IntegrationFlowContext integrationFlowContext;
  private final MqttPahoClientFactory mqttClientFactory;

  public static final String MQTT_DYNAMIC_TOPIC_1 = "/TOPIC1";
  public static final String MQTT_DYNAMIC_TOPIC_2 = "/TOPIC2";

  public void createMqttInboundFlow() {
    IntegrationFlow dynamicIntegrationFlow1 = IntegrationFlows
        .from(mqttChannelAdapter(MQTT_DYNAMIC_TOPIC_1))
        .handle(message -> log.info(message.toString()))
        .get();
    integrationFlowContext.registration(dynamicIntegrationFlow1).id(MQTT_DYNAMIC_TOPIC_1).register();

    IntegrationFlow dynamicIntegrationFlow2 = IntegrationFlows
        .from(mqttChannelAdapter(MQTT_DYNAMIC_TOPIC_2))
        .handle(message -> log.info(message.toString()))
        .get();
    integrationFlowContext.registration(dynamicIntegrationFlow2).id(MQTT_DYNAMIC_TOPIC_2).register();

    log.info("key set : {}", integrationFlowContext.getRegistry().keySet());
  }

  public void removeMqttInboundFlow() {
    this.registryKeySet().forEach(integrationFlowContext::remove);
  }

  public Set<String> registryKeySet() {
    return integrationFlowContext.getRegistry().keySet();
  }


  private MqttPahoMessageDrivenChannelAdapter mqttChannelAdapter(String topic) {
    MqttPahoMessageDrivenChannelAdapter adapter =
        new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, UUID.randomUUID().toString(), mqttClientFactory, topic);
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    return adapter;
  }
}
