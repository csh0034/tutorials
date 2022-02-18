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
Spring Boot 2.6.x, Spring Cloud 2021.0.1 기준 Spring Cloud Netflix Eureka 만 남아있다.

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

> 필터는 크게 Predicate 에 해당 될 경우 처리되는 **GatewayFilter** 와 조건부로 모든 Route 에 적용되는 **GlobalFilter** 가 있다.

### 동작 원리

![img.png](images/01.png)

- 출처 : [카카오 광고 플랫폼 MSA 적용 사례 및 API Gateway 와 인증 구현에 대한 소개](https://www.slideshare.net/ifkakao/msa-api-gateway)

## 개발환경
- spring boot 2.6.3
- spring cloud 2021.0.1
- Maven

pom.xml

```xml
<properties>
  <spring-cloud.version>2021.0.1</spring-cloud.version>
</properties>
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>${spring-cloud.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
  </dependency>
  
  <!-- Controller 기반 라우팅 처리시 필요함  -->
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-gateway-webflux</artifactId>
  </dependency>

  <!-- JWT 인증처리 에서 사용  -->
  <dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.18.3</version>
  </dependency>
</dependencies>
```

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
                  }))
          .uri("http://localhost:9999"))
    
      .route("debug_route", r -> r
          .order(-1)
          .query("debug", "1")
          .filters(f -> f
              .filter(debugGatewayFilter))
          .uri("http://localhost:9999"))
      .build();
}
```

## Controller 를 통한 Gateway 설정

spring-cloud-gateway-webflux 의존성이 있어야함

```java
@RestController
public class GatewayController {

  @GetMapping("/proxy")
  public Mono<ResponseEntity<String>> proxy(ProxyExchange<String> proxy) {
    return proxy.uri("http://localhost:9000/proxy")
        .header("sample", "value")
        .forward();
  }

}
```

## Actuator API

Actuator 의존성을 통하여 gateway Endpoint 를 노출하면 `/actuator/gateway/routes` 경로를 통하여 정보 확인 가능

```yaml
management:
  endpoints:
    web:
      exposure:
        include:
          - "gateway"
```

Sample

```json
[
  {
    "predicate": "Paths: [/user/**], match trailing slash: true",
    "metadata": {
      "sampleKey": "sampleValue"
    },
    "route_id": "user_route",
    "filters": [
      "[[RewritePath /user/?(?<segment>.*) = '/${segment}'], order = 1]"
    ],
    "uri": "http://localhost:9999",
    "order": 0
  },
  {
    "predicate": "Paths: [/board/**], match trailing slash: true",
    "route_id": "board_route",
    "filters": [
      "[[RewritePath /board/?(?<segment>.*) = '/${segment}'], order = 1]"
    ],
    "uri": "http://localhost:9999",
    "order": 0
  },
  {
    "predicate": "Paths: [/**], match trailing slash: true",
    "route_id": "default_route",
    "filters": [
      
    ],
    "uri": "http://localhost:9999",
    "order": 0
  }
]
```

## Gateway 를 통한 JWT 인증 필터 구현

필터를 구현하기 위해선 모든 요청에 인증을 할 경우엔 **GlobalFilter** 로 구현하고  
predicate 를 사용하여 특정 조건에서만 인증을 할 경우엔 **GatewayFilter** 로 구현한다.

하단 구현에선 `GatewayFilter` 로 처리하였음.

### JWT 인증을 처리를 위한 클래스

```java
@Component
@ConfigurationProperties("jwt")
@Setter
@Getter
public class JwtProperties {

  private String secret;

  // jwt 발급 테스트를 위해 추가함, Gateway 는 토큰 인증만 하고 발급은 처리 하지 않기 때문에 운영시엔 불필요함.
  private long expirationSecond;

}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TokenUser {

  private String id;
  private String role;

}

@Component
@RequiredArgsConstructor
public class JwtUtils implements InitializingBean {

  private static final String ROLE_CLAIM_KEY = "role";

  private final JwtProperties jwtProperties;

  private Algorithm algorithm;
  private JWTVerifier jwtVerifier;

  @Override
  public void afterPropertiesSet() {
    this.algorithm = Algorithm.HMAC512(jwtProperties.getSecret());
    this.jwtVerifier = JWT.require(algorithm).acceptLeeway(5).build();
  }

  public boolean isValid(String token) {
    try {
      jwtVerifier.verify(token);
      return true;
    } catch (RuntimeException e){
      return false;
    }
  }

  public TokenUser decode(String token) {
    jwtVerifier.verify(token);

    DecodedJWT jwt = JWT.decode(token);

    String id = jwt.getSubject();
    String role = jwt.getClaim(ROLE_CLAIM_KEY).asString();

    return new TokenUser(id, role);
  }

  // Gateway 는 Decode 만 필요하지만 테스트에서 필요하기 때문에 package-private 으로 설정
  String generate(TokenUser user) {
    Date now = new Date();
    Date expiresAt = new Date(now.getTime() + jwtProperties.getExpirationSecond() * 1000);

    return JWT.create()
        .withSubject(user.getId())
        .withClaim(ROLE_CLAIM_KEY, user.getRole())
        .withExpiresAt(expiresAt)
        .withIssuedAt(now)
        .sign(algorithm);
  }

}
```

### JwtAuthenticationGatewayFilterFactory

GatewayFilter 를 구현하기 위해선 AbstractGatewayFilterFactory 를 구현해야하며  
커스텀 필터의 이름은 **GatewayFilterFactory** 로 끝나야하며 suffix 를 제외한 이름으로 참조 가능하다.

하단 클래스의 경우 `JwtAuthentication` 으로 참조 가능

> suffix 없이 클래스 이름을 지정할 수 있지만 공식 지원하는 네이밍 컨벤션은 아님.

```java
@Component
@Slf4j
public class JwtAuthenticationGatewayFilterFactory extends
    AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

  private static final String ROLE_KEY = "role";

  private final JwtUtils jwtUtils;

  public JwtAuthenticationGatewayFilterFactory(JwtUtils jwtUtils) {
    super(Config.class);
    this.jwtUtils = jwtUtils;
  }

  @Override
  public List<String> shortcutFieldOrder() {
    return Collections.singletonList(ROLE_KEY);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      ServerHttpResponse response = exchange.getResponse();

      if (!containsAuthorization(request)) {
        return onError(response, "missing authorization header", HttpStatus.BAD_REQUEST);
      }

      String token = extractToken(request);
      if (!jwtUtils.isValid(token)) {
        return onError(response, "invalid authorization header", HttpStatus.BAD_REQUEST);
      }

      TokenUser tokenUser = jwtUtils.decode(token);
      if (!hasRole(tokenUser, config.role)) {
        return onError(response, "invalid role", HttpStatus.FORBIDDEN);
      }

      addAuthorizationHeaders(request, tokenUser);

      return chain.filter(exchange);
    };
  }

  private boolean containsAuthorization(ServerHttpRequest request) {
    return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
  }

  private String extractToken(ServerHttpRequest request) {
    return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
  }

  private boolean hasRole(TokenUser tokenUser, String role) {
    return role.equals(tokenUser.getRole());
  }

  private void addAuthorizationHeaders(ServerHttpRequest request, TokenUser tokenUser) {
    request.mutate()
        .header("X-Authorization-Id", tokenUser.getId())
        .header("X-Authorization-Role", tokenUser.getRole())
        .build();
  }

  private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
    response.setStatusCode(status);
    DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Mono.just(buffer));
  }

  @Setter
  public static class Config {

    private String role;

  }

}
```

application.yml
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: partner_route
          uri: http://localhost:9999
          predicates:
            - Host=partner.example.com
          filters:
            - JwtAuthentication=ROLE_PARTNER

        - id: auth_route
          uri: http://localhost:9999
          predicates:
            - Path=/auth/**
          filters:
            - JwtAuthentication=ROLE_ADMIN

        - id: default_route
          uri: http://localhost:9999
          predicates:
            - Path=/**

jwt:
  secret: secret
  expiration-second: 28800
```

## JWT 인증 필터 테스트

### Downstream 세팅

Gateway 기능을 테스트 하기위해 downstream 역할의 샘플 프로젝트를 9999 포트로 구성 후 하단 컨트롤러 추가.  

> 요청 받은 헤더를 모두 출력한다.

```java
@RestController
public class HeaderPrintController {

  @GetMapping("/{path}")
  public Map<String, String> path(@PathVariable String path, @RequestHeader HttpHeaders headers) {
    Map<String, String> map = headers.toSingleValueMap();
    map.put("path", path);
    return map;
  }

}
```

### Curl 을 통한 요청

#### 성공

```shell
$ curl --verbose -H 'Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBU2siLCJyb2xlIjoiUk9MRV9BRE1JTiIsImV4cCI6MTk1OTkzODg0MiwiaWF0IjoxNjQ0NTc4ODQyfQ.pn_PIOGTmTfY9a9Nham6vAXq3y--BRRG2bIvNku15WmlQtLoea6ATTp-ktaKJOggmiEP6Z6rq-HBg3wuHBHo9Q' http://localhost:8080/auth

*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /auth HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
> Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBU2siLCJyb2xlIjoiUk9MRV9BRE1JTiIsImV4cCI6MTk1OTkzODg0MiwiaWF0IjoxNjQ0NTc4ODQyfQ.pn_PIOGTmTfY9a9Nham6vAXq3y--BRRG2bIvNku15WmlQtLoea6ATTp-ktaKJOggmiEP6Z6rq-HBg3wuHBHo9Q
>
< HTTP/1.1 200 OK
< transfer-encoding: chunked
< Content-Type: application/json
< Date: Sat, 12 Feb 2022 10:45:13 GMT
```

Response Body Formatting

```json
{
  "user-agent": "curl/7.64.1",
  "accept": "*/*",
  "authorization": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBU2siLCJyb2xlIjoiUk9MRV9BRE1JTiIsImV4cCI6MTk1OTkzODg0MiwiaWF0IjoxNjQ0NTc4ODQyfQ.pn_PIOGTmTfY9a9Nham6vAXq3y--BRRG2bIvNku15WmlQtLoea6ATTp-ktaKJOggmiEP6Z6rq-HBg3wuHBHo9Q",
  "x-authorization-id": "ASk",
  "x-authorization-role": "ROLE_ADMIN",
  "forwarded": "proto=http;host=\"localhost:8080\";for=\"[0:0:0:0:0:0:0:1]:64290\"",
  "x-forwarded-for": "0:0:0:0:0:0:0:1",
  "x-forwarded-proto": "http",
  "x-forwarded-port": "8080",
  "x-forwarded-host": "localhost:8080",
  "host": "localhost:9999",
  "content-length": "0",
  "path": "auth"
}
```

#### 실패, 토큰 없음

```shell
$ curl --verbose http://localhost:8080/auth

*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /auth HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
>
< HTTP/1.1 400 Bad Request
< content-length: 28
<
* Connection #0 to host localhost left intact
missing authorization header* Closing connection 0
```

#### 실패, invalid 토큰

```shell
$ curl --verbose -H 'Authorization: invalid' http://localhost:8080/auth

*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /auth HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
> Authorization: invalid
>
< HTTP/1.1 400 Bad Request
< content-length: 28
<
* Connection #0 to host localhost left intact
invalid authorization header* Closing connection
```

#### 실패, 권한 없음 (ROLE_USER 권한으로 요청함)

```shell
$ curl --verbose -H 'Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBU2siLCJyb2xlIjoiUk9MRV9VU0VSIiwiZXhwIjoxOTU5ODU3NDI3LCJpYXQiOjE2NDQ0OTc0Mjd9.Gc7FqMIAkCpQPGEf6DelEfGbqi7pWGbtZYO4Qif2tdQOaIf7bmqptDPzr2tNG8FnvXxnAXPnEZnYSWimNpfbUg' http://localhost:8080/auth

*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /auth HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
> Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBU2siLCJyb2xlIjoiUk9MRV9VU0VSIiwiZXhwIjoxOTU5ODU3NDI3LCJpYXQiOjE2NDQ0OTc0Mjd9.Gc7FqMIAkCpQPGEf6DelEfGbqi7pWGbtZYO4Qif2tdQOaIf7bmqptDPzr2tNG8FnvXxnAXPnEZnYSWimNpfbUg
>
< HTTP/1.1 403 Forbidden
< content-length: 12
<
* Connection #0 to host localhost left intact
invalid role* Closing connection 0
```

## 참조

- [Spring Cloud Gateway, Reference](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Spring Cloud Gateway, 번역](https://godekdls.github.io/Spring%20Cloud%20Gateway/contents/)
- [Blog 1](https://cheese10yun.github.io/spring-cloud-gateway/)
