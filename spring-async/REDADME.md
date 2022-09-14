# Spring Async

## Awaitility

### Awaitility 란?

비동기식 시스템 테스트 DSL 을 지원하는 라이브러리

### Default 값 세팅
```java
Awaitility.setDefaultTimeout(..)   // 10s
Awaitility.setDefaultPollInterval(..) // 100ms
Awaitility.setDefaultPollDelay(..)  // null
Awaitility.reset(); // 초기화
```

### Usage

```java
await().until(fieldIn(object).ofType(int.class).andWithName("fieldName"), equalTo(2));

await().atMost(5, TimeUnit.SECONDS)
    .untilAsserted(() -> assertThat(fakeRepository.getValue()).isEqualTo(1));
```

## 참조

- [GitHub wiki, awaitility](https://github.com/awaitility/awaitility/wiki/Usage)
