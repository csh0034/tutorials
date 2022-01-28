# Spring Cloud Stream Producer

## Functional 방식

### Binding Name 규칙
**Input** : `<function name> + -in- + <index>`  
**Output** : `<function name> + -out- + <index>`

### Functional Interface
@Bean 으로 등록된 `Supplier<T>`, `Function<T, R>`, `Consumer<T>` 를 찾아  
Function 메서드 이름과 Property 에 설정된 binding Name 을 자동으로 연결

#### Supplier
Spring Integration 의 PollerMetadata 에 polling mechanism 에 의해 호출된다.  
디폴트 1초마다 호출, null 리턴하면 처리안됨

## 참조
- [Spring Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/3.2.1/reference/html/)
- [Spring Cloud Stream Samples, GitHub](https://github.com/spring-cloud/spring-cloud-stream-samples)
- [Blog 1](https://medium.com/@odysseymoon/spring-cloud-stream-with-rabbitmq-c273ed9a79b)
