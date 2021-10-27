# Spring Cloud Config Client

개발환경
- IntelliJ IDEA 2021.2.3
- spring boot 2.5.5
- spring cloud 2020.0.4
- Java 8
- Maven

pom.xml
```xml
<properties>
  <java.version>1.8</java.version>
  <spring-cloud.version>2020.0.4</spring-cloud.version>
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

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
application.yml
```yaml
spring:
  application:
    name: api
  profiles:
    active: dev
  config:
    import: "optional:configserver:"

  cloud:
    config:
      uri: "http://localhost:9999" # spring config server uri
      name: core,${spring.application.name} # Multiple name
```

## 원격 리소스 가져오기
- application : `spring.application.name`
- profile : `spring.profiles.active`
- label : Git 을 사용할 경우 branch (default: master)
> spring.cloud.config.*(name, profile, label) 로 대체 가능

## multiple name
> spring.cloud.config.name=core,api
- Config Server 에서 해당하는 두가지 이름 모두 가져옴
- NativeEnvironmentRepository getEnvironment 메서드 호출시에 spring.config.name 으로 추가하여 처리함
- Spring Cloud Config GitHub Test 에 Multiple name 을 지정한 테스트가 있음
- [NativeEnvironmentRepositoryTests locationPlaceholdersMultipleApplication](https://github.com/spring-cloud/spring-cloud-config/blob/04e1d22b13/spring-cloud-config-server/src/test/java/org/springframework/cloud/config/server/environment/NativeEnvironmentRepositoryTests.java)
- 우선 Native (File System) 모드에선 정상 작동함 다른곳에서도 동작하는지 테스트 해봐야함


## [spring.config.import](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#config-data-import)
> spring.config.import=optional:configserver:
- 해당 프로퍼티 추가시에 default Config Server 주소로 연결 : `http://localhost:8888`
- 주소 변경 방법
  1. 위의 방법 추가 + `spring.cloud.config.uri=http://myhost:8888`
  2. `spring.config.import=optional:configserver:http://myhost:8888`
  
## [Actuator 를 이용한 refresh](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#endpoints)
Actuator `RefreshEndpoint` 접근시에 EnvironmentChangeEvent 발생 시킴
- /actuator/refresh

## [@RefreshScope](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#refresh-scope)
- refresh 할때 해당 어노테이션이 있는 대상 갱신해줌

## [POST 를 통해 env 변수 변경하기](https://docs.spring.io/spring-cloud-commons/docs/endpoints/reference/html/#endpoints)
- management.endpoint.env.post.enabled=true
- `WritableEnvironmentEndpointAutoConfiguration` 동작함

## [Spring Cloud Bus 를 활용한 refresh](https://docs.spring.io/spring-cloud-bus/docs/current/reference/html/)
- [Spring Cloud Bus, Github](https://github.com/spring-cloud/spring-cloud-bus)
- amqp, kafka (RabbitMQ 또는 Kafka) 를 이용 하여 구성 추가할 경우
  - spring-cloud-starter-bus-amqp
  - spring-cloud-starter-bus-kafka
- Actuator 의 [/actuator/busrefresh](https://github.com/spring-cloud/spring-cloud-bus/blob/main/spring-cloud-bus/src/main/java/org/springframework/cloud/bus/endpoint/RefreshBusEndpoint.java) 호출 시에 연결된 모든 client 에 /actuator/refresh 엔드포인트에  
  모두 ping을 받은 것처럼 각 애플리케이션의 구성을 다시 로드

## 로컬에서 스프링 클라우드를 실행할수 없는 경우
```java
@Configuration
public class MyConfiguration {
    
    @Configuration
    @Profile("local")
    @PropertySource(value = "classpath:application-testConfig-local.yml")
    static class DefaultConfig {
        // member fields
    }
}
```