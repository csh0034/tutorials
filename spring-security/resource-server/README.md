# Spring Security Resource Server

## Settings
개발환경
- spring boot 2.6.4
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
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

application.yml
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          public-key-location: classpath:simple.pub # static 방식
#          jwk-set-uri: http://localhost:9000/oauth2/jwks #JWT + JWK 방식
```

## Decoder
org.springframework.security.oauth2.jwt.JwtDecoder (Interface)
- JWT(JSON Web Token)를 컴팩트 클레임 표현 형식에서 Jwt 객체로 변환한다.

org.springframework.security.oauth2.jwt.NimbusJwtDecoder
- JwtDecoder 구현체, Builder 및 정적 팩토리 메서드 지원
- PublicKeyJwtDecoderBuilder 의 경우 기본 RSA(RS256) 방식
- SecretKeyJwtDecoderBuilder 의 경우 기본 Hmac(HS256) 방식

OAuth2ResourceServerJwtConfiguration
- Spring boot 사용시 프로퍼티 기반 jwk url, public-key-location 을 지정하여 사용 가능하다.

## EdDSA

- Edwards-curve Digital Signature Algorithm
- 서명체계로는 25519 커브를 베이스로 하는 Ed25519 가 있다.
- 64바이트 서명 값을 생성, 공개키 개인키가 32바이트

### Octet key pair

- Edwards-curve 키를 나타내는데 사용된다.
- JWK 유형 지정시 "OKP" 를 사용한다.

## 참조
- [Spring Security, OAuth 2.0 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Spring Security GitHub, OAuth 2.0 Migration Guide](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide)
- [How to generate a JSON Web Key (JWK)](https://connect2id.com/products/nimbus-jose-jwt/examples/jwk-generation)
- [GitHub, noimbus jose jwt](https://github.com/felx/nimbus-jose-jwt)
