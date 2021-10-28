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

## Config Server 에서 설정 정보 가져오는 시점
- application 이 올라 갈때 가져오는것이 아닌 호출시마다 갱신해서 가져옴
- client 측에서 EnvironmentController 의 설정 정보를 가져오는 api 호출시에  
  EnvironmentRepository.findOne 을 호출하며 그시점에 가져온다.

## [Environment Repository](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_environment_repository)
- Git (GitRepositoryConfiguration)
  - default 설정임, `@Profile("git")` 
  - github 사용시 suffix `.git` 필수아님.
  - gitlab 사용시에 suffix `.git` 필수임. 없으면 _422 Unprocessable Entity_ 발생 
```yaml
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://gitlab.com/csh0034/spring-cloud-config-repo.git
```
- File System (NativeRepositoryConfiguration)
  - `@Profile("native")`
  - search-locations 설정 안할 경우 검색 위치
    - "optional:classpath:/"
    - "optional:classpath:/config/"
    - "optional:file:./"
    - "optional:file:./config/"
```yaml
spring:
  application:
    name: config-server
  profiles:
    active: native
#  cloud:
#    config:
#      server:
#        native:
#          search-locations: classpath:/sample-config/
```
- Redis
- Database
- ...