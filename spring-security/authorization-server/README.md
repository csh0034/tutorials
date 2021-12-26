# Spring Security Authorization Server

## Settings
개발환경
- spring boot 2.6.2
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

## String to Rsa Object
- RsaKeyConversionServicePostProcessor
  - @Value 에 rsa pub/pri key 또는 키의 위치를 명시하면 자동으로 바꿔줌
  - String 값이 "-----" 로 시작할 경우 직접 바꾸며 아닐 경우 Resource 위치로 처리함 
  
## 참조
- [spring-authorization-server github](https://github.com/spring-projects/spring-authorization-server)
- [OAuth 2.0 Migration Guide](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide)