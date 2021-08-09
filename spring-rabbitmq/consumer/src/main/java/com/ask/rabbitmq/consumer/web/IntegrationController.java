package com.ask.rabbitmq.consumer.web;

import com.ask.rabbitmq.consumer.config.MqttIntegrationUtils;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class IntegrationController {

  private final MqttIntegrationUtils mqttIntegrationUtils;

  @GetMapping("/integration")
  public String find() {
    Set<String> keySet = mqttIntegrationUtils.registryKeySet();
    log.info("key set : {}", keySet);
    return keySet.toString();
  }

  @GetMapping("/integration/add")
  public String add() {
    mqttIntegrationUtils.createMqttInboundFlow();
    return mqttIntegrationUtils.registryKeySet().toString();
  }

  @GetMapping("/integration/remove")
  public String remove() {
    mqttIntegrationUtils.removeMqttInboundFlow();
    return mqttIntegrationUtils.registryKeySet().toString();
  }
}
