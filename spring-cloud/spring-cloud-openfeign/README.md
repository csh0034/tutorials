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

### Feign Logging

생성된 각 Feign 클라이언트에 대해 로거가 생성되며 Feign 클라이언트 생성에 이용된 인터페이스의 전체 클래스이름이다.  
또한 debug level 로 로그를 출력한다.

Bean 으로 Logger.Level 를 설정해야 한다.

- NONE : 로깅 없음(default).
- BASIC : request method, url, response status code, 실행 시간만 기록한다.
- HEADERS : 요청 및 응답 헤더와 함께 기본 정보를 기록한다.
- FULL : 요청과 응답 모두에 대한 헤더, 본문 및 메타데이터를 기록한다.

feign 클라이언트가 있는 패키지 debug 설정
```yaml
logging:
  level:
    "[com.ask.openfeign]": debug
```

```java
import feign.Logger;

@Bean
public Logger.Level feignLoggerLevel() {
  return Logger.Level.BASIC;
}
```

### [Reactive Support?](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#reactive-support)

현재 OpenFeign project 는 Spring WebClient 와 같은 reactive client 를 지원하지 않는다.  
따라서 Spring Cloud OpenFeign 도 지원하지 않고있다. OpenFeign project 에서 지원하게 되면 바로 추가 예정이다.

이 작업이 완료될 때까지 [feign-reactive](https://github.com/Playtika/feign-reactive) 사용을 추천한다.

## Troubleshooting

### resilience4j 적용 이후 Retry 시도 1초 넘어가면 오류 발생

```text
java.util.concurrent.TimeoutException: TimeLimiter 'FacebookClient#notFound()' recorded a timeout exception.
```

Resilience4J TimeLimiter 의 기본 timeout 값이 1초로 설정되어있음.  
Customizer 를 통하여 기본설정을 5초로 변경하였음.

```java
@Bean
public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
  return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
      .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(5)).build())
      .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
      .build());
}
```

실제 커넥션 타임아웃등의 처리는 Feign 설정으로 하는게 낫다.

```java
import feign.Request;

@Bean
public Request.Options requestOptions() {
  return new Request.Options(3, TimeUnit.SECONDS, 3, TimeUnit.SECONDS, true);
}
```

### jsr-310(Java8 Date-Time) 파라미터 타입 사용시 포맷 오류

feign client 에서 java8 Date-Time 관련 파라미터타입을 사용할 경우 전송시에 포맷이 하단과 같이 처리됨

#### 설정 이전 로그

```text
[SampleClient#time] ---> GET http://localhost:8080/time?startDt=22.%202.%2020%20%EC%98%A4%ED%9B%84%2011%3A48 HTTP/1.1
```

@Configuration 을 추가하여 FeignClient 에 대해 전역설정으로 DateTimeFormatter 를 적용

```java
@Configuration
public class DefaultFeignClientConfig {

  private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Bean
  public FeignFormatterRegistrar defaultDateTimeFormatterRegistrar() {
    return registry -> {
      DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
      registrar.setDateFormatter(dateFormatter);
      registrar.setTimeFormatter(timeFormatter);
      registrar.setDateTimeFormatter(dateTimeFormatter);
      registrar.registerFormatters(registry);
    };
  }

}
```

단순하게 ISO Format 으로 사용 할 경우

```java
@Configuration
public class DefaultFeignClientConfig {

  @Bean
  public FeignFormatterRegistrar defaultDateTimeFormatterRegistrar() {
    return registry -> {
      DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
      registrar.setUseIsoFormat(true);
      registrar.registerFormatters(registry);
    };
  }

}
```

#### 설정 이후 로그

```text
[SampleClient#time] ---> GET http://localhost:8080/time?startDt=2022-02-20%2023%3A49%3A02 HTTP/1.1
```

#### 추가사항

Feign Client 선언시 파라미터에 @DateTimeFormat 추가해도 처리됨

## 참조
- [Spring Cloud OpenFeign, Reference](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)
- [Spring Cloud Circuit Breaker, Reference](https://docs.spring.io/spring-cloud-circuitbreaker/docs/current/reference/html/)
- [OpenFeign, GitHub](https://github.com/OpenFeign/feign)
- [netflix feign vs openfeign, Baeldung](https://www.baeldung.com/netflix-feign-vs-openfeign)
- [spring-cloud-feign-resilience4j-timelimiter](https://arnoldgalovics.com/spring-cloud-feign-resilience4j-timelimiter/)
