package com.ask.rabbitmq.consumer.config;

import static com.ask.rabbitmq.consumer.config.MqttConfig.BROKER_URL;
import static com.ask.rabbitmq.consumer.config.MqttConfig.MQTT_TOPIC;

import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;


/**
 * producer QUEUE_NAME_1에 메세지 전송 defaultFlow(QUEUE_NAME_1) 에서 처리 후 routeToRecipients channel1(defaultSubFlow1),
 * channel2(defaultSubFlow2) channel2(defaultSubFlow2) 에서 QUEUE_NAME_2 에 메세지 전송 defaultFlow2(QUEUE_NAME_2) 에서 처리
 * <p>
 * IntegrationFlow 동적 생성 및 추가 방법 1. integrationFlowContext 주입 받는다 ( 프레임워크에서 자동으로 bean 생성 ) 2.
 * integrationFlowContext.registration(IntegrationFlow).id(ID).register();
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AmqpConfig {

  private final ConnectionFactory connectionFactory;
  private final TopicActivator topicActivator;
  private final MqttPahoClientFactory mqttClientFactory;
  private final IntegrationFlowContext integrationFlowContext;

  public static final String EXCHANGE_NAME = "sample.exchange";
  public static final String QUEUE_NAME_1 = "sample.queue1";
  public static final String QUEUE_NAME_2 = "sample.queue2";
  public static final String QUEUE_NAME_3 = "sample.queue3";
  public static final String ROUTING_KEY = "sample.routing.key.#";

  public static final String DIRECT_CHANNEL1 = "directChannel1";
  public static final String DIRECT_CHANNEL2 = "directChannel2";

  public static final String LOGGING_CHANNEL = "loggingChannel";

  @PostConstruct
  public void init() {
    log.info(integrationFlowContext.toString());
  }

  /*
  @RabbitListener 사용하여 메세지 처리
  @RabbitListener(bindings = @QueueBinding(
      exchange = @Exchange(name = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
      value = @Queue(name = QUEUE_NAME),
      key = ROUTING_KEY
  ))
  public void receiveMessage(final Map<String, String> message) {
    log.info(message.toString());
  }
   */

  //@Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean(name = DIRECT_CHANNEL1)
  public MessageChannel channel1() {
    return new DirectChannel();
  }

  @Bean(name = DIRECT_CHANNEL2)
  public MessageChannel channel2() {
    return new DirectChannel();
  }

  // 인바운드 게이트웨이는 일부 다른 시스템이 메시징 애플리케이션을 호출하고 응답을 수신하는 양방향 통합 플로우에 사용
  //@Bean
  public IntegrationFlow amqpInboundGateway() {
    return IntegrationFlows.from(inboundGateway(connectionFactory, QUEUE_NAME_1).defaultReplyTo(QUEUE_NAME_3))
        .transform(Transformers.fromJson())
        .get();
  }

  // 인바운드 채널 어댑터는 데이터를 메시징 애플리케이션으로 가져오기 위한 단방향 통합에 사용
  @Bean
  public IntegrationFlow defaultFlow() {
    return IntegrationFlows
        .from(Amqp.inboundAdapter(simpleMessageListenerContainer(connectionFactory, QUEUE_NAME_1)))
        .transform(Transformers.fromJson())
        //.transform(new JsonToObjectTransformer())
        .<Map<String, String>>handle((p, h) -> topicActivator.handle(p))
        .filter(this::logging)
        .log()
        .log(Level.ERROR, "log.test", m -> m)
        .wireTap(LOGGING_CHANNEL)
        .routeToRecipients(
            r -> r.applySequence(true)
                .recipient(channel1())
                .recipient(channel2()))
        .get();
  }

  private SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory, String queueName) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setDefaultRequeueRejected(false);
    container.setConcurrentConsumers(2);
    // container.setConsumerBatchEnabled();
    // container.setPrefetchCount(250);
    return container;
  }

  private boolean logging(String message) {
    log.info("filter : " + message);
    return true;
  }

  @Bean
  public IntegrationFlow defaultFlow2() {
    return IntegrationFlows
        .from(Amqp.inboundAdapter(connectionFactory, QUEUE_NAME_2))
        .handle(message -> log.info("defaultFlow2 :" + message.getPayload()))
        .get();
  }

  @Bean
  public IntegrationFlow mqttFlow() {
    return IntegrationFlows
        .from(mqttChannelAdapter())
        .handle(message -> log.info(message.toString()))
        .get();
  }

  //spring.jmx.enabled false 해도 켜지는 이유 : intellij spring boot run/debug Enable jmx agent 켜져있음
  @Bean
  public MBeanExporter mbeanExporter() {
    MBeanExporter mBeanExporter = new MBeanExporter();
    mBeanExporter.setAutodetectMode(MBeanExporter.AUTODETECT_MBEAN);
    mBeanExporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
    mBeanExporter.setNamingStrategy(new MetadataNamingStrategy(new AnnotationJmxAttributeSource()));
    return mBeanExporter;
  }

  private MqttPahoMessageDrivenChannelAdapter mqttChannelAdapter() {
    //BROKER_URL 없을 경우 MqttConnectOptions serverURIs 사용
    MqttPahoMessageDrivenChannelAdapter adapter =
        new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, MqttAsyncClient.generateClientId(), mqttClientFactory, MQTT_TOPIC);
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    //adapter.setOutputChannel();
    return adapter;
  }

  @Bean
  public IntegrationFlow defaultSubFlow1() {
    return IntegrationFlows
        .from(channel1())
        .handle(msg -> topicActivator.handleChannel1((String) msg.getPayload()))
        .get();
  }

  @Bean
  public IntegrationFlow defaultSubFlow2(RabbitTemplate rabbitTemplate) {
    return IntegrationFlows
        .from(channel2())
        .handle(Amqp.outboundAdapter(rabbitTemplate).routingKey(QUEUE_NAME_2))
        .get();
  }

  @Bean(name = LOGGING_CHANNEL)
  public MessageChannel loggingChannel() {
    return MessageChannels.direct().get();
  }

  @Bean
  public IntegrationFlow logFlow() {
    return IntegrationFlows.from(LOGGING_CHANNEL).handle(loggingHandler()).get();
  }

  @Bean
  public LoggingHandler loggingHandler() {
    //loggingHandler.setShouldLogFullMessage(true); // 메세지 전체 보여줄지 여부
    //loggingHandler.setLoggerName("integrationLogger"); // log 출력 이름
    return new LoggingHandler(LoggingHandler.Level.INFO.name());
  }

  @Component
  public static class TopicActivator {

    public String handle(Map<String, String> map) {
      log.info("TopicActivator handle : " + map);
      return map.get("timestamp");
    }

    public void handleChannel1(String timestamp) {
      log.info("TopicActivator handleChannel1 : " + timestamp);
    }

    public void handleChannel2(String timestamp) {
      log.info("TopicActivator handleChannel2 : " + timestamp);
    }
  }
}
