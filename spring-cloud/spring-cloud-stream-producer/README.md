# Spring Cloud Stream Producer

## Spring Cloud Stream
Message 기반 마이크로 서비스 어플리케이션을 만들기위한 Framework 로 메시징 시스템과   
연동하여 확장성 높은 이벤트 기반 마이크로 서비스를 구축 가능하게 해준다.

Spring Integration 을 기반으로 높은 추상화 수준의 API 를 제공한다.

3.x 버전 부터 spring-cloud-stream-reactive 가 중단 되었으며 Spring Cloud Function 을 지원함에 따라  
@EnableBinding, @StreamListener 등의 Annotation-based programming model 이 사용되지 않고 Function 기반으로 처리된다.

> Servlet 방식보다 Reactive 방식으로 적용하는게 나아 보인다.

## Functional Programming Model

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
