package com.ask.mqtt.web;

import com.ask.mqtt.MqttConnections;
import com.ask.mqtt.config.MQttTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MqttController {

  private final MqttConnections mqttConnections;

  @GetMapping
  public String mqtt(String url) {
    mqttConnections.publish(url, MQttTopic.MQTT_TOPIC, 2, String.valueOf(System.currentTimeMillis()));
    return "success";
  }

}
