package com.ask.rabbitmq.consumer.config;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
public class MqttConfig {

  public static final String BROKER_URL = "tcp://localhost:1883";
  public static final String MQTT_TOPIC = "/TOPIC";

  public static final String MQTT_OUTBOUND_CHANNEL = "outboundChannel";

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

//    MqttPahoMessageDrivenChannelAdapter getConnectionInfo() 참고
//    세팅 되어있지 않을 경우 Adapter Url 사용
    options.setServerURIs(new String[]{BROKER_URL});
    options.setCleanSession(true);
    return options;
  }

  @Bean(name = MQTT_OUTBOUND_CHANNEL)
  public MessageChannel mqttOutboundChannel() {
    return new DirectChannel();
  }

  /**
   * 선언전 MqttOutboundFlow 생성
   */
  //@Bean
  public IntegrationFlow mqttOutboundFlow() {
    return IntegrationFlows
        .from(mqttOutboundChannel())
        .handle(mqttOutboundMessageHandler()).get();
  }

  private MessageHandler mqttOutboundMessageHandler() {
    MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory());
    messageHandler.setAsync(true);
    messageHandler.setDefaultQos(1);
    return messageHandler;
  }

  @MessagingGateway(defaultRequestChannel = MQTT_OUTBOUND_CHANNEL, defaultRequestTimeout = "5000", defaultReplyTimeout = "5000")
  public interface MqttOutboundGateway {

    @Gateway
    void publish(@Header(MqttHeaders.TOPIC) String topic, String data);

    @Gateway
    void publish(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer qos, String data);
  }
}
