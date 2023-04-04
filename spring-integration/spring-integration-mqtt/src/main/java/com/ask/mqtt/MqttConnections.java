package com.ask.mqtt;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class MqttConnections implements InitializingBean {

  private LoadingCache<String, MqttPahoMessageHandler> caches;

  @Override
  public void afterPropertiesSet() {
    caches = CacheBuilder.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .removalListener((RemovalListener<String, MqttPahoMessageHandler>) removal -> {
          MqttPahoMessageHandler handler = removal.getValue();
          handler.stop();
        })
        .build(new CacheLoader<>() {
          @Override
          public MqttPahoMessageHandler load(String key) {
            MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(key, MqttClient.generateClientId(),
                createMqttClientFactory());
            messageHandler.setCompletionTimeout(5000);
            messageHandler.afterPropertiesSet();
            return messageHandler;
          }
        });
  }

  private MqttPahoClientFactory createMqttClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    MqttConnectOptions options = new MqttConnectOptions();
    // options...
    factory.setConnectionOptions(options);
    return factory;
  }

  public void publish(String url, String topic, int qos, String payload) {
    MqttPahoMessageHandler mqttPahoMessageHandler = get(url);
    Message<String> message = MessageBuilder.withPayload(payload)
        .setHeader(MqttHeaders.TOPIC, topic)
        .setHeader(MqttHeaders.QOS, qos)
        .build();

    try {
      mqttPahoMessageHandler.handleMessage(message);
    } catch (Exception e) {
      caches.invalidate(url);
      throw new RuntimeException(String.format("mqtt publish failed..url:%s, topic:%s, qos:%s", url, topic, qos), e);
    }
  }

  private MqttPahoMessageHandler get(String url) {
    try {
      return caches.get(url);
    } catch (ExecutionException e) {
      throw new RuntimeException("Failed to load MqttPahoMessageHandler for url: " + url);
    }
  }

}
