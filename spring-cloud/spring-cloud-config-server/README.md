# Spring Cloud Config Server

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
Java Config
```java
@SpringBootApplication
@EnableConfigServer   // Cloud Config Server 설정
public class SpringCloudConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudConfigServerApplication.class, args);
  }
}
```
application.yml
```yaml
server:
  port: 9999

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: "*"

spring:
  application:
    name: config-server
  profiles:
    active: native    # File System 의 config 를 사용
# 파일 경로 지정이 필요할 경우 세팅
# default, "optional:classpath:/", "optional:classpath:/config/", "optional:file:./", "optional:file:./config/"
#  cloud:
#    config:
#      server:
#        native:
#          search-locations: classpath:/sample-config/
```

## HTTP 호출을 통한 리소스 형식
`EnvironmentController` 에서 처리
- application : `spring.application.name`
- profile : `spring.profiles.active`
- label : Git 을 사용할 경우 branch (default: master)
```text
GET /{application}/{profile}[/{label}]
GET /{application}-{profile}.yml
GET /{label}/{application}-{profile}.yml
GET /{application}-{profile}.properties
GET /{label}/{application}-{profile}.properties
```

## [Environment Repository](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_environment_repository)
- Git
- File System (NativeRepositoryConfiguration)
- Redis
- Database
- ...