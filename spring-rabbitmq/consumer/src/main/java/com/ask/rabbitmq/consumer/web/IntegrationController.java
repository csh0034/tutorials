package com.ask.rabbitmq.consumer.web;

import com.ask.rabbitmq.consumer.config.DynamicIntegrationUtils;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class IntegrationController {

  private final DynamicIntegrationUtils dynamicIntegrationUtils;

  @GetMapping("/integration")
  public String find() {
    Set<String> keySet = dynamicIntegrationUtils.registryKeySet();
    log.info("key set : {}", keySet);
    return keySet.toString();
  }

  @GetMapping("/integration/add")
  public String add() {
    dynamicIntegrationUtils.createMqttInboundFlow();
    return dynamicIntegrationUtils.registryKeySet().toString();
  }

  @GetMapping("/integration/remove")
  public String remove() {
    dynamicIntegrationUtils.removeMqttInboundFlow();
    return dynamicIntegrationUtils.registryKeySet().toString();
  }
}
