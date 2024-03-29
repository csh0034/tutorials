package com.ask.mqtt;

import com.ask.mqtt.config.MQttTopic;
import com.ask.mqtt.config.MqttConfig.MqttOutboundGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendRunner implements ApplicationRunner {

  private final MqttOutboundGateway mqttOutboundGateway;
  private final MqttConnections mqttConnections;

  @Override
  public void run(ApplicationArguments args) {
    mqttOutboundGateway.publish(MQttTopic.MQTT_TOPIC, 2, "hello mqttOutboundGateway");

    MqttUtils.publishAndDisconnect("tcp://localhost:1883", MQttTopic.MQTT_TOPIC, 2, "hello MqttUtils");

    mqttConnections.publish("tcp://localhost:1883", MQttTopic.MQTT_TOPIC, 2, "hello MqttConnections");
  }

}
