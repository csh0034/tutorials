package com.ask.rabbitmq.consumer.web;

import com.ask.rabbitmq.consumer.config.MqttConfig;
import com.ask.rabbitmq.consumer.config.MqttIntegrationUtils;
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
  private final MqttIntegrationUtils mqttIntegrationUtils;

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
}
