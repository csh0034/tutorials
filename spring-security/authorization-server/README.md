# Spring Security Authorization Server

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
  <version>0.3.10</version>
</dependency>
```

application.yml
```yaml
server:
  port: 9000
```

## OAuth2TokenEndpointFilter

### The supported authorization grant types

- authorization_code
- client_credentials
- refresh_token

## String to Rsa Object

- RsaKeyConversionServicePostProcessor
  - @Value 에 rsa pub/pri key 또는 키의 위치를 명시하면 자동으로 바꿔줌
  - String 값이 "-----" 로 시작할 경우 직접 바꾸며 아닐 경우 Resource 위치로 처리함 

## Troubleshooting

### ClientSecretAuthenticationProvider

헤더에 전달된 client_id, client_secret 을 검증하는 역할을 한다.    
이때 client_secret 검증을 위해 PasswordEncoder 를 사용하는데   
OAuth2ClientAuthenticationConfigurer.createDefaultAuthenticationProviders() 에서  
Bean 을 통해서만 주입되어 변경이 불가능하다.

## 참조

- [GitHub, spring authorization server](https://github.com/spring-projects/spring-authorization-server)
- [GitHub, spring security samples](https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/oauth2/authorization-server/build.gradle)
- [OAuth 2.0 Migration Guide](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide)
