# Spring Cloud OpenFeign

## [OpenFeign 이란?](https://github.com/OpenFeign/feign)

선언적 Http Client 이며 인터페이스와 어노테이션 기반으로 쉽게 작성 가능하다.  
임계치 조건에따라 Fallback 처리등의 다양한 기능을 지원한다.

원래 Netflix OSS 의 일부로 Netflix 에서 만들고 출시하였으나  
현재는 OpenFeign 이라는 이름으로 Feign 을 오픈소스 커뮤니티로 완전히 이전 하였다.

기존엔 `spring-cloud-starter-feign` 으로 사용하였으나 현재는 `spring-cloud-starter-openfeign` 으로 변경됨.

## Spring Cloud OpenFeign 이란?

Spring Boot 환경에서 Spring 이 제공하는 어노테이션등을 활용하여 OpenFeign 과 통합을 제공한다. 

인터페이스에 @RequestMapping 등의 어노테이션으로 선언할 경우 Spring 이 런타임에 구현체를 제공한다.

### 내부 사용 Http Client

기본적으로 `HttpURLConnection` 을 사용하여 호출하며 클래스패스의 라이브러리 추가후  
프로퍼티 설정을 할 경우 다른 Http Client 를 사용하게 설정할 수 있다.

### Spring Cloud CircuitBreaker 지원

Spring Cloud CircuitBreaker 가 클래스 경로에 있고 `feign.circuitbreaker.enabled=true` 프로퍼티를 설정할 경우  
Feign 이 모든 메서드를 circuit breaker 로 감싼다.

pom.xml
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

### [Reactive Support?](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#reactive-support)

현재 OpenFeign project 는 Spring WebClient 와 같은 reactive client 를 지원하지 않는다.  
따라서 Spring Cloud OpenFeign 도 지원하지 않고있다. OpenFeign project 에서 지원하게 되면 바로 추가 예정이다.

이 작업이 완료될 때까지 [feign-reactive](https://github.com/Playtika/feign-reactive) 사용을 추천한다.

## 참조
- [Spring Cloud OpenFeign, Reference](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)
- [OpenFeign, GitHub](https://github.com/OpenFeign/feign)
- [netflix feign vs openfeign, Baeldung](https://www.baeldung.com/netflix-feign-vs-openfeign)
