package com.ask.rabbitmq.consumer.config;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
public class MqttConfig {

  public static final String BROKER_URL = "tcp://localhost:1883";
  public static final String MQTT_CLIENT_ID = MqttAsyncClient.generateClientId();
  public static final String MQTT_TOPIC = "/TOPIC";

  @Bean
  public MqttPahoClientFactory mqttPahoClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    factory.setConnectionOptions(connectOptions());
    return factory;
  }

  private MqttConnectOptions connectOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
//    options.setUserName(MQTT_USERNAME);
//    options.setPassword(MQTT_PASSWORD.toCharArray());
    options.setCleanSession(true);
    return options;
  }
}
