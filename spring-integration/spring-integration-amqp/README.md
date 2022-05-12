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



