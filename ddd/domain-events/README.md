# Domain Events

## 도메인 이벤트 구현

### 1. Service 에서 Event 발행

### 2. Domain 에서 static method 호출하여 event 발행

### 3. @DomainEvents 를 사용하여 event 발행

### 4. AbstractAggregateRoot 를 사용하여 event 발행

내부에서 @DomainEvents 사용함

## Troubleshooting

### 변경감지 사용시 동작안함

@DomainEvents 기능은 Spring Data 리포지토리를 사용할 때만 동작 한다.

`EventPublishingRepositoryProxyPostProcessor` 에서 동작 조건은 하단과 같다.

```java
private static boolean isEventPublishingMethod(Method method) {
  return method.getParameterCount() == 1
    && (isSaveMethod(method.getName()) || isDeleteMethod(method.getName()));
}

private static boolean isSaveMethod(String methodName) {
  return methodName.startsWith("save");
}

private static boolean isDeleteMethod(String methodName) {
  return methodName.equals("delete") || methodName.equals("deleteAll") || methodName.equals("deleteInBatch")
    || methodName.equals("deleteAllInBatch");
}
```

### jpa callback method 에서 사용시 유의 사항

`@PostPersist`, `@PostUpdate` 등에서 `registerEvent()` 를 실행할 경우 동작 하지 않는다.

해당 PostProcessor 는 레포지토리 메서드가 수행되면 바로 실행되지만 `@PostXXX` 는 트랜잭션이 commit 되고  
쿼리가 나간 이후에 실행되므로 이벤트가 처리되는 시점엔 등록 되어있지 않는 상태이다.

## 참조

- [baeldung, DDD Aggregates and @DomainEvents](https://www.baeldung.com/spring-data-ddd)
