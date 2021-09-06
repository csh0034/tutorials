# spring-jpa-lock

## 스프링 부트 + JPA LOCK
개발환경
- IntelliJ IDEA 2021.2.1
- spring boot 2.5.3
- Java 8
- Maven
- MariaDB 10.4.20

pom.xml
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.mariadb.jdbc</groupId>
  <artifactId>mariadb-java-client</artifactId>
</dependency>
```

application.yml
```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/lock
    username: root
    password: 111111

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        format_sql: true
    open-in-view: false

logging:
  level:
    org.hibernate:
      SQL: debug
```

***
JPA Lock
