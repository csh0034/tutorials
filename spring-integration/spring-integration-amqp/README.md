# Spring Integration Amqp

## Configuration

### SimpleRabbitListenerContainerFactory

- SimpleMessageListenerContainer 를 빌드하기위한 팩토리
- SpringBoot 의 경우 RabbitAnnotationDrivenConfiguration 에서 Bean 으로 등록됨

```java
SimpleMessageListenerContainer listenerContainer = rabbitListenerContainerFactory.createListenerContainer();
```

### RabbitAnnotationDrivenConfiguration 에서 Bean 선언 부분 

```java
@Bean(name = "rabbitListenerContainerFactory")
@ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
@ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = "type", havingValue = "simple", matchIfMissing = true)
SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
    SimpleRabbitListenerContainerFactoryConfigurer configurer, 
    ConnectionFactory connectionFactory,
    ObjectProvider<ContainerCustomizer<SimpleMessageListenerContainer>> simpleContainerCustomizer) {
  SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
  configurer.configure(factory, connectionFactory);
  simpleContainerCustomizer.ifUnique(factory::setContainerCustomizer);
  return factory;
}
```

### ContainerCustomizer\<C extends MessageListenerContainer>

- SimpleRabbitListenerContainerFactory 에 customizing 을 할 수 있다.
- 생성되는 SimpleMessageListenerContainer 에 적용된다.
- `ObjectProvider ifUnique` 로 동작 하므로 하나만 Bean 으로 등록해야함

```java
@Bean
public ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer() {
  return container -> {
    container.setDefaultRequeueRejected(false);
    container.setConcurrentConsumers(2);
  };
}
```

### 
```java
SimpleMessageListenerContainer listenerContainer = rabbitListenerContainerFactory.createListenerContainer();
listenerContainer.setQueueNames(QueueConstants.TEST_QUEUE_NAME);
```

### amqp integration flow 사용시 thread pool 지정

기본적으론 `Amqp.inboundAdapter` 에 의해 생성되거나 주입된 SimpleMessageListenerContainer 의 thread pool 을 사용함

`AbstractMessageListenerContainer.initialize()` 를 보면 매번 스레드 생성

```java
public void initialize() {
  // ...
  if (!this.taskExecutorSet && StringUtils.hasText(getListenerId())) {
    this.taskExecutor = new SimpleAsyncTaskExecutor(getListenerId() + "-");
    this.taskExecutorSet = true;
  }
  // ...
}
```

#### executor 세팅 방법

1. SimpleRabbitListenerContainerFactory 를 사용한다면 하단과 같이 등록

```java
@Bean
public ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer(ThreadPoolTaskExecutor executor) {
  return container -> {
    container.setTaskExecutor(executor);
  };
}
```
2. ingeration flow 에 connectionFactory 를 사용 하는 경우

```java
Amqp.inboundAdapter(connectionFactory, QueueConstants.TEST_QUEUE_NAME)
  .configureContainer(spec -> spec.taskExecutor(taskExecutor))
```
