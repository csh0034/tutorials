package com.ask.integration.consumer.web;

import com.ask.integration.consumer.config.MqttConfig;
import com.ask.integration.consumer.config.MqttIntegrationUtils;
import com.ask.integration.consumer.config.MqttPubSubChannelConfig;
import com.ask.integration.consumer.config.MqttPubSubChannelConfig.SampleMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MqttController {

  private final MqttConfig.MqttOutboundGateway mqttOutboundGateway;
  private final MqttPubSubChannelConfig.MqttPubSubOutboundGateway mqttPubSubOutboundGateway;
  private final MqttIntegrationUtils mqttIntegrationUtils;
  private final ObjectMapper objectMapper;

  @GetMapping("/mqtt")
  public String mqtt(@RequestParam(defaultValue = "test") String message) {
    try {
      mqttOutboundGateway.publish(MqttConfig.MQTT_TOPIC, message);
    } catch (Exception e) {
      log.info("outbound handler is not registered");
    }
    return message;
  }

  @GetMapping("/mqtt/add")
  public String add() {
    mqttIntegrationUtils.createMqttOutboundFlow();
    return mqttIntegrationUtils.registryKeySet().toString();
  }

  @GetMapping("/mqtt/remove")
  public String remove() {
    mqttIntegrationUtils.removeMqttOutboundFlow();
    return mqttIntegrationUtils.registryKeySet().toString();
  }

  @GetMapping("/mqtt/pub-sub")
  public String pubSub(@RequestParam(defaultValue = "1") String id) throws Exception {
    String mqttHandlerId = MqttPubSubChannelConfig.MQTT_HANDLER_1;
    if (!id.equals("1")) {
      mqttHandlerId = MqttPubSubChannelConfig.MQTT_HANDLER_2;
    }

    String payload = objectMapper.writeValueAsString(new SampleMessage(0, "ask"));
    mqttPubSubOutboundGateway.publish(mqttHandlerId, MqttPubSubChannelConfig.MQTT_PUB_SUB_TOPIC, payload);
    return "success";
  }
}
