# Spring Event

## 개발환경
- IntelliJ IDEA 2021.2.3
- spring boot 2.5.6
- Java 8
- Maven
- H2 1.4.200

## custom event 생성
- ApplicationEvent 를 상속받아 구현
```java
public class SampleEvent extends ApplicationEvent {

  private static final long serialVersionUID = 7160735850460474302L;

  private final String message;

  public SampleEvent(Object source, String message) {
    super(source);
    this.message = message;
  }
}
```

## publisher 를 통한 이벤트 호출
```java
@SpringBootTest
class SampleEventListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @SpyBean
  private SampleEventListener sampleEventListener;

  @DisplayName("ApplicationListener 구현 방식")
  @Test
  void sampleEvent() {
    // given
    SampleEvent event = new SampleEvent("source!!!", "SampleEventListenerTest!!!");

    // when
    publisher.publishEvent(event);

    // then
    then(sampleEventListener).should(times(1)).onApplicationEvent(any());
  }

}
```

## Annotation-Driven Event Listener
- Spring 4.2부터 @EventListener 를 사용하여 Annotation 기반 이벤트 처리 지원
- condition : 이벤트 처리를 조건부로 만드는 데 사용되는 SpEL(Spring Expression Language) 표현식
```java
@Component
@Slf4j
public class SampleEventAnnotationDrivenListener {

  @EventListener(condition = "#event.success")
  public void handleSampleEvent(SampleEvent event) {
    log.info("SampleEvent : {}", event);
  }
}
```

- 여러 이벤트를 수신하거나, 매개변수 없이 정의하려는 경우 사용 방법
```java
@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
public void handleContextStart() {
    // ...
}
```

## Asynchronous event
- spring event 는 기본적으로 동기적으로 호출된다. 같은 쓰레드에서 실행됨
- @Async 를 사용하여 비동기 이벤트 처리 가능
- 주의 사항
  - 비동기식 이벤트 리스너에서 Exception이 발생 하면 호출자에게 전파되지 않는다.
  - 비동기식 이벤트 리스너 메서드는 값을 반환하여 후속 이벤트를 게시할 수 없다.
```java
@Component
@Slf4j
public class AsyncEventAnnotationDrivenListener {

  @Async
  @EventListener
  public void handleAsyncEvent(AsyncEvent event) {
    log.info("AsyncEvent : {}", event);
  }
}
```

## Listener Ordering
- @org.springframework.core.annotation.Order 를 사용 동일 이벤트에 대해 순서 처리 가능
```java
@EventListener
@Order(42)
public void processBlockedListEvent(BlockedListEvent event) {
    // notify appropriate parties via notificationAddress...
}
```

## [Generic Events](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-events-generics)
- Spring 4.2부터 모든 객체(Object)를 이벤트로서 발급할 수 있다. (`AbstractApplicationContext.publishEvent`)
- 파라미터가 ApplicationEvent 타입이 아닐 경우, `PayloadApplicationEvent` 로 감싸서 처리한다.
```java
@RequiredArgsConstructor
@Getter
@ToString
public class GenericEvent<T> {

  private final T entity;
  private final boolean needToPersist;
}
```
```java
@Component
@Slf4j
@RequiredArgsConstructor
public class GenericEventAnnotationDrivenListener {

  private final EntityManager em;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @EventListener(condition = "#event.needToPersist")
  public void handleGenericEvent(GenericEvent<?> event) {
    log.info("GenericEvent<User> : {}", event);
    em.persist(event.getEntity());
  }
}
```
```java
@SpringBootTest
class GenericEventAnnotationDrivenListenerTest {

  @Autowired
  private ApplicationEventPublisher publisher;

  @SpyBean
  private GenericEventAnnotationDrivenListener genericEventAnnotationDrivenListener;

  @DisplayName("제네릭 이벤트, condition : true")
  @Test
  void genericEventWithConditionTrue() {
    // given
    User user = User.create("ask-true");
    GenericEvent<User> event = new GenericEvent<>(user, true);

    // when
    publisher.publishEvent(event);

    // then
    then(genericEventAnnotationDrivenListener).should(atLeastOnce()).handleGenericEvent(any());
  }

  @DisplayName("제네릭 이벤트, condition : false")
  @Test
  void genericEventWithConditionFalse() {
    // given
    User user = User.create("ask-false");
    GenericEvent<User> event = new GenericEvent<>(user, false);

    // when
    publisher.publishEvent(event);

    // then
    then(genericEventAnnotationDrivenListener).should(never()).handleGenericEvent(any());
  }
}
```


## [@TransactionalEventListener](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-event)
- 트랜잭션의 결과가 리스너 처리에 중요한 요소일때 유연하게 사용할 수 있다.
- 실행 중인 트랜잭션이 없으면 의미적으로 필요가 없기때문에 리스너가 동작하지 않는다.
- fallbackExecution : 실행 중인 트랜잭션이 없는 경우 이벤트를 처리해야 하는지 여부
  - boolean (default false)
- TransactionPhase : 트랜잭션 이벤트 리스너가 적용되는 단계
  - before-commit
  - after-commit (default)
  - after-rollback
  - after-completion

|익셉션 발생|호출 이벤트|
|---|---|
| O |after-rollback -> after-completion|
| X |before-commit -> after-commit -> after-completion|

```java
@Component
@Slf4j
public class TransactionEventAnnotationDrivenListener {

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void beforeCommit(TransactionEvent event) {
    log.info("TransactionEvent (BEFORE_COMMIT) : {}", event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void afterCommit(TransactionEvent event) {
    log.info("TransactionEvent (AFTER_COMMIT) : {}", event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void afterRollback(TransactionEvent event) {
    log.info("TransactionEvent (AFTER_ROLLBACK) : {}", event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void afterCompletion(TransactionEvent event) {
    log.info("TransactionEvent (AFTER_COMPLETION) : {}", event);
  }
}
```

## 참조
- [Spring, Standard and Custom Events](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-events)
- [Spring Boot, Application Events and Listeners](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.spring-application.application-events-and-listeners)