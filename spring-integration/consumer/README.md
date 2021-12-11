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