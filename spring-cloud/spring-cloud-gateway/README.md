# Spring Cloud Gateway

## Spring Cloud 란 ?
Spring Cloud 는 분산 시스템 및 MSA 환경에서의 일부 공통 패턴들을 빠르게 구축할 수 있도록 지원한다.

### Spring Cloud 지원 기능
- Distributed/versioned configuration
- Service registration and discovery
- Intelligent Routing
- Service-to-service calls
- Load balancing
- Circuit Breakers
- Distributed messaging
- ...

### Spring Cloud Netflix
Netflix OSS(Open Source Software) 와 스프링에 통합을 지원한다.  
Spring Boot 2.6.x, Spring Cloud 2021.0.0 기준 Spring Cloud Netflix Eureka 만 남아있다.

[Replacements, Spring Blog](https://spring.io/blog/2018/12/12/spring-cloud-greenwich-rc1-available-now)

![02.png](images/02.png)

## Spring Cloud Gateway 란?

SCG 는 **Spring 5, Spring Boot 2, Project Reactor** 를 기반으로 API Gateway 를 구축을 지원한다.  
API 라우팅 및 보안, 모니터링/메트릭, resiliency 등의 기능을 간단하고 효과적인 방법으로 제공한다.

> spring-mvc, spring-data, spring-security 등 동기방식의 프로젝트들과 함께 실행하면 문제가 발생할 수 있다.

**Netflix Zuul** 대신 Spring 에서 권장하는 gateway 서비스이다.  
Spring Boot 2.4.x 이상부터 Zuul 1 을 사용할 수 없으며 Zuul 2 와 통합은 지원하지 않는다.

### Netflix Zuul

Netflix OSS 에 포함된 컴포넌트 중 하나로서 API Gateway 패턴을 구현한다.

- Zuul 1 : 서블릿 프레임워크 기반이며 동기(Synchronous), 블로킹(Blocking) 방식
- Zuul 2 : Netty 기반의 비동기(asynchronous), 논블로킹(non-blocking) 방식

## 용어 및 동작 원리

### 용어

| 명칭             | 설명                                                                       |
|----------------|--------------------------------------------------------------------------|
| 라우트(Route)     | 라우트는 고유 ID, 목적지 URI, 조건자 목록, 필터 목록으로 정의된다. <br/> 모든 조건자가 충족됐을 때만 매칭된다    |
| 조건자(Predicate) | 각 요청을 처리하기 전에 실행되는 로직, 헤더와 파라미터 값 등 다양한 HTTP 요청이 <br/>정의된 기준에 맞는지를 확인한다. |
| 필터(Filter)     | 다운스트림으로 요청을 전송하기 전후에 요청과 응답을 수정할 수 있다. <br> 필수 구성요소는 아님.                 |

### 동작 원리

![img.png](images/01.png)

- 출처 : [카카오 광고 플랫폼 MSA 적용 사례 및 API Gateway 와 인증 구현에 대한 소개](https://www.slideshare.net/ifkakao/msa-api-gateway)

## YML 을 통한 Gateway 설정

### Route Predicate Factories

- 필수 : uri, predicates
- 선택 : filters

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: user_route
        uri: http://localhost:9999
        metadata:
          sampleKey: sampleValue    # 필터에서 사용 가능한 별도 파라미터
        predicates:
          - After=2022-01-01T08:00:00.000+09:00[Asia/Seoul]
          - Before=2022-12-31T22:00:00.000+09:00[Asia/Seoul]
          - Between=2022-01-01T08:00:00.000+09:00[Asia/Seoul], 2022-12-31T22:00:00.000+09:00[Asia/Seoul]
          - Cookie=chocolate, ch.p
          - Header=X-Request-Id, \d+
          - Host=**.example.com,**.example.org  # Host Header 기반
          - Method=GET,POST
          - Path=/user/{segment}                # Path 기반
          - Query=green
          - Query=debug, true
          - RemoteAddr=192.168.1.1/24
          - #...
```

### GatewayFilter Factories

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: user_route
        uri: lb://user
        predicates:
          - Path=/user/**
        filters:
          - AddRequestHeader=X-Request-red, blue
          - AddRequestParameter=red, blue
          - AddResponseHeader=X-Response-Red, Blue
          - PrefixPath=/v1
          - RedirectTo=302, https://acme.org
          - RewritePath=/user/?(?<segment>.*), /$\{segment}
          - SetPath=/{segment}
          - #...
```

## Java Config 을 통한 Gateway 설정

```java
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder, DebugGatewayFilter debugGatewayFilter) {
  return builder.routes()
      .route("order_route", r -> r
          .path("/order/**")
          .filters(f -> f
              .rewritePath("/order/(.*)", "/$1")
              .addRequestHeader("X-Order-Header", "order"))
          .metadata("orderKey", "orderValue")
          .uri("http://localhost:9999"))
    
      .route("host_rewrite_route", r -> r
          .host("*.test.com")
          .filters(f -> f
              .prefixPath("/v1")
              .addResponseHeader("X-TestHeader", "rewrite_empty_response")
              .modifyResponseBody(String.class, String.class,
                  (exchange, s) -> {
                    if (s == null) {
                      return Mono.just("emptybody");
                    }
                    return Mono.just(s.toUpperCase());
                  })
          ).uri("http://localhost:9999"))
    
      .route("debug_route", r -> r
          .order(-1)
          .query("debug", "1")
          .filters(f -> f
              .filter(debugGatewayFilter))
          .uri("http://localhost:9999"))
      .build();
}
```

## 참조

- [Spring Cloud Gateway, Reference](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Spring Cloud Gateway, 번역](https://godekdls.github.io/Spring%20Cloud%20Gateway/contents/)
- [Blog 1](https://cheese10yun.github.io/spring-cloud-gateway/)
