# spring boot + actuator

## actuator  
어플리케이션을 모니터링하고 관리하는 기능을 Spring Boot에서 자체적으로 제공, HTTP와 JMX를 통해 확인 가능

### [Endpoint List](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)
- beans : 애플리케이션에 있는 모든 Spring 빈의 전체 목록을 표시
- caches : 사용가능한 cache를 노출
- conditions : 구성 및 자동 구성 클래스에서 평가된 조건과 일치하거나 일치하지 않는 이유를 표시
- env : Spring ConfigurableEnvironment
- health : 애플리케이션 상태 정보 표시
- httptrace : http를 trace한 정보를 노출
- mappings : Request와 mapping되어있는 handler 정보를 가져옵니다.
- sessions : Spring Session이 가지고 있는 정보를 가져옵니다.
- threaddump : threaddump를 가져옴
- logfile : log를 가져옵니다.
- metrics : metrics 정보를 노출합니다
- ...

### application.yml
```yaml
management:
#  health:
#    db:
#      enabled: false
  endpoints:
    web:
      exposure:
        include: "*" # http 를 통하여 모든 정보 노출
  endpoint:
    health:
      show-details: always
  server:
    address: 127.0.0.1 # 127.0.0.1 으로만 접근가능
    port: 8081  # actuator port 다르게 설정
```

### [Writing Custom HealthIndicators](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health.writing-custom-health-indicators)
- HealthIndicator에 대한 식별자는 빈 이름 에서 HealthIndicator 접미사를 제외하고 노출됨
- 하단의 경우 `custom` 으로 노출됨
```java
@Component
public class CustomHealthIndicator extends AbstractHealthIndicator {

  @Override
  protected void doHealthCheck(Builder builder) {
    builder.up()
        .withDetail("app", "running")
        .withDetail("error", "nothing");
  }
}
```

### [spring-boot-admin-starter-client](https://codecentric.github.io/spring-boot-admin/2.4.3/#register-client-applications)
pom.xml
```xml
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-client</artifactId>
  <version>2.4.3</version>
</dependency>

<!-- build-info 사용시에 app 목록에 버전정보 표시됨 -->
<plugin>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-maven-plugin</artifactId>
 <executions>
   <execution>
     <goals>
       <goal>build-info</goal>
     </goals>
   </execution>
 </executions>
</plugin>
```

application.yml
```yaml
spring:
  boot:
    admin:
      client:
        url: "http://localhost:18080"
```




## 참조
- [Spring Boot, Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)