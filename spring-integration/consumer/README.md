# RabbitMQ Consumer

## [IntegrationFlow 동작 구조](https://docs.spring.io/spring-integration/reference/html/dsl.html#java-dsl-channels)
- dsl 방식으로 선언하면 컴포넌트 체이닝시에 내부적으로 DirectChannel 을 생성한다. 
  - pattern : `[IntegrationFlow.beanName].channel#[channelNameIndex]`
  - .channel("채널 명) 을 통해 output channel 이름 명시 가능
- 내부적으로 flow 를 처리할 handler 를 Set Collection 에 보관 하므로 같은객체일 경우 두번 handling 되지 않는다.
- 아래와 같을 경우 filter가 추가되어 있기 때문에 Set 에 handler, MessageFilter 가 등록되어 있다.
- 따라서 메세지가 두번 처리됨, filter가 없을 경우 한번만 처리됨
```java
// 채널 이름 예시
// 체이닝 하는 대상이 채널 일경우 채널의 bean 이름으로 생성된다.
public IntegrationFlow mqttPubSubInboundFlow1() {
  return IntegrationFlows
      .from(..) // mqttPubSubInboundFlow1.channel#0 
      .transform(...) // mqttPubSubInboundFlow1.channel#1
      .handle(...) // mqttPubSubInboundFlow1.channel#2
      .get();
}
```
```java
@Bean
public IntegrationFlow mqttPubSubOutboundFlow1() {
  return IntegrationFlows
      .from(mqttPubSubOutboundChannel())
      .log("mqttPubSubOutboundFlow1")
      .handle(customMqttOutboundMessageHandler) // pubSubOutboundChannel 을 구독하고있음
      .get();
}

@Bean
public IntegrationFlow mqttPubSubOutboundFlow2() {
  return IntegrationFlows
      .from(mqttPubSubOutboundChannel())
      .log("mqttPubSubOutboundFlow2")
      .filter((MessageSelector) message -> true)  // 처리후 mqttPubSubOutboundFlow2.channel#1 로 보냄
      .handle(customMqttOutboundMessageHandler)   // mqttPubSubOutboundFlow2.channel#1 을 구독하고 있음
      .get();
}
```
```text
EventDrivenConsumer     : Adding {message-handler} as a subscriber to the 'pubSubOutboundChannel' channel
PublishSubscribeChannel : Channel 'application.pubSubOutboundChannel' has 1 subscriber(s).
EventDrivenConsumer     : started bean 'mqttPubSubOutboundFlow1...

EventDrivenConsumer     : Adding {filter} as a subscriber to the 'pubSubOutboundChannel' channel
PublishSubscribeChannel : Channel 'application.pubSubOutboundChannel' has 2 subscriber(s).
EventDrivenConsumer     : started bean 'mqttPubSubOutboundFlow2...

EventDrivenConsumer     : Adding {message-handler} as a subscriber to the 'mqttPubSubOutboundFlow2.channel#1' channel
DirectChannel           : Channel 'application.mqttPubSubOutboundFlow2.channel#1' has 1 subscriber(s).
```

## Integration 사용시 Mbean 이슈
- Integration Flows Bean 생성시에 Bean 이름에 자동으로 `:`(콜론) 이 들어가면서 생성이 안되는 이슈
- 하단 의존성 추가
```xml
<dependency>
  <groupId>org.springframework.integration</groupId>
  <artifactId>spring-integration-jmx</artifactId>
</dependency>
```
- [Github issue, spring-integration](https://github.com/spring-projects/spring-integration/issues/3051)

## Integration Flow Error 처리

DefaultConfiguringBeanFactoryPostProcessor registerErrorChannel 에서 기본적으로 처리된다.  
`errorChannel` 이라는 이름의 채널을 등록하지 않을경우 자동으로 PublishSubscribeChannel 로 생성하여 Bean 으로 등록한다. 

또한 LoggingHandler 를 Bean 으로 등록한후에 `errorChannel` subscribe 한다.  
따라서 수동으로 설정해야 할 경우 반드시 `errorChannel` 을 bean 으로 등록해야 LoggingHandler 가 자동 등록 되지 않는다.

### MQTT error channel

MqttPahoMessageDrivenChannelAdapter 사용시에 error channel 세팅을 하지 않을 경우  
예외 발생시 커넥션이 잠깐 끊기게 된다. (그동안 메세지 못받음)

error channel 을 등록하면 예외를 채널로 보내서 예외를 throw 하지 않으므로 커넥션이  
끊기지 않게된다.

### 수동 error channel 설정

```java
public static String ERROR_CHANNEL_NAME = "errorChannel";

// 수동으로 errorChannel 등록.
@Bean(ERROR_CHANNEL_NAME)
public MessageChannel errorChannel() {
  return MessageChannels.publishSubscribe().get();
}

// errorChannel 을 처리하는 Flow 등록
@Bean
public IntegrationFlow errorHandlingFlow() {
  return IntegrationFlows
    .from(ERROR_CHANNEL_NAME)
    .handle(message -> log.error("error occurred!! : {}", message.getPayload()))
    .get();
}

// adapter 등록시에 error channel 등록
public MqttPahoMessageDrivenChannelAdapter mqttChannelAdapter(String url, String topic, MqttPahoClientFactory mqttClientFactory) {
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(url, MqttAsyncClient.generateClientId(), mqttClientFactory, topic);
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    adapter.setErrorChannelName(ERROR_CHANNEL_NAME);
    return adapter;
}
```

