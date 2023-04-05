package com.ask.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public final class MqttUtils {

  public static void publishAndDisconnect(String brokerUrl, String topic, int qos, String payload) {
    String clientId = MqttClient.generateClientId();
    MqttClient mqttClient = null;

    MqttConnectOptions connectOptions = new MqttConnectOptions();
    connectOptions.setConnectionTimeout(10);

    try {
      mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
      mqttClient.connect(connectOptions);

      MqttMessage mqttMessage = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
      mqttMessage.setQos(qos);
      mqttClient.publish(topic, mqttMessage);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(String.format("mqtt publish failed..url:%s, topic:%s, qos:%s", brokerUrl, topic, qos), e);
    } finally {
      closeMqttClient(mqttClient);
    }
  }

  public static void closeMqttClient(MqttClient mqttClient) {
    if (mqttClient != null) {
      disconnect(mqttClient);
      close(mqttClient);
    }
  }

  private static void disconnect(MqttClient mqttClient) {
    if (mqttClient.isConnected()) {
      try {
        mqttClient.disconnect();
      } catch (MqttException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private static void close(MqttClient mqttClient) {
    try {
      mqttClient.close();
    } catch (MqttException e) {
      log.error(e.getMessage(), e);
    }
  }

  public static void checkAlive(String brokerUrl) {
    CountDownLatch latch = new CountDownLatch(1);

    String clientId = MqttClient.generateClientId();
    MqttClient mqttClient = null;

    MqttConnectOptions connectOptions = new MqttConnectOptions();
    connectOptions.setConnectionTimeout(10);

    try {
      mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
      mqttClient.connect(connectOptions);

      mqttClient.setCallback(new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
          latch.countDown();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }
      });

      mqttClient.subscribe("PNS/ALIVE", 0);
      mqttClient.publish("PNS/ALIVE", "pong".getBytes(), 0, false);

      boolean success = latch.await(5, TimeUnit.SECONDS);

      if (success) {
        log.info("pns({}) check success...", brokerUrl);
      } else {
        throw new RuntimeException(String.format("pns(%s) check fails-> code:1", brokerUrl));
      }

    } catch (MqttException | InterruptedException e) {
      throw new RuntimeException(String.format("pns(%s) check fails-> code:2", brokerUrl), e);
    } finally {
      closeMqttClient(mqttClient);
    }
  }

}
