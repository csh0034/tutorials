package com.ask.mqtt;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public final class MqttUtils {

  public static void publishAndDisconnect(String brokerUrl, String topic, int qos, String payload) {
    String clientId = MqttClient.generateClientId();
    MqttClient mqttClient = null;
    try {
      mqttClient = new MqttClient(brokerUrl, clientId);
      mqttClient.connect();

      MqttMessage mqttMessage = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
      mqttMessage.setQos(qos);
      mqttClient.publish(topic, mqttMessage);

    } catch (Exception e) {
      throw new RuntimeException(String.format("mqtt publish failed..url:%s, topic:%s, qos:%s", brokerUrl, topic, qos), e);
    } finally {
      closeMqttClient(mqttClient);
    }
  }

  private static void closeMqttClient(MqttClient mqttClient) {
    if (mqttClient != null && mqttClient.isConnected()) {
      try {
        mqttClient.disconnect();
        mqttClient.close();
      } catch (MqttException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

}
