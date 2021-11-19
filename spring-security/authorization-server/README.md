# Spring Security Authorization Server

## Settings
개발환경
- IntelliJ IDEA 2021.2.3
- spring boot 2.5.6
- Java 8
- Maven

pom.xml
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-oauth2-authorization-server</artifactId>
  <version>0.2.0</version>
</dependency>
```

application.yml
```yaml
server:
  port: 9000
```

## 참조
- [spring-authorization-server github](https://github.com/spring-projects/spring-authorization-server)
- [OAuth 2.0 Migration Guide](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide)