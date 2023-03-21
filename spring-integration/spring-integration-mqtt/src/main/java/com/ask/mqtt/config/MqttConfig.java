package com.ask.mqtt.config;

import com.ask.mqtt.config.handler.MqttInboundHandler;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
@RequiredArgsConstructor
public class MqttConfig {

  private final MqttInboundHandler mqttInboundHandler;

  @Bean
  public MqttPahoClientFactory mqttPahoClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    factory.setConnectionOptions(connectionOptions());
    return factory;
  }

  private MqttConnectOptions connectionOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setServerURIs(new String[]{"tcp://localhost:1883"});
    return options;
  }

  @Bean
  public IntegrationFlow mqttInboundFlow() {
    return IntegrationFlows.from(mqttChannelAdapter())
        .transform(Transformers.objectToString())
        .handle(message -> mqttInboundHandler.handle((String) message.getPayload()))
        .get();
  }

  private MqttPahoMessageDrivenChannelAdapter mqttChannelAdapter() {
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(MqttClient.generateClientId(),
        mqttPahoClientFactory(), MQttTopic.MQTT_TOPIC);
    adapter.setCompletionTimeout(5_000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    return adapter;
  }

  @Bean
  public IntegrationFlow mqttOutboundFlow() {
    return IntegrationFlows
        .from(MqttChannels.MQTT_OUTBOUND_CHANNEL)
        .handle(mqttOutboundMessageHandler())
        .get();
  }

  private MessageHandler mqttOutboundMessageHandler() {
    MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(),
        mqttPahoClientFactory());
    messageHandler.setDefaultQos(1);
    return messageHandler;
  }

  @MessagingGateway(defaultRequestChannel = MqttChannels.MQTT_OUTBOUND_CHANNEL)
  public interface MqttOutboundGateway {

    @Gateway
    void publish(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String data);

  }

}
