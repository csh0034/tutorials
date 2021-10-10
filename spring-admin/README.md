# spring boot admin

### spring-boot-admin-starter-admin
pom.xml
```xml
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-server</artifactId>
  <version>2.4.3</version>
</dependency>
```
```java
@SpringBootApplication
@EnableAdminServer  // admin 활성화
public class SpringAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAdminApplication.class, args);
  }

}
```

### [spring boot admin, security](https://codecentric.github.io/spring-boot-admin/2.4.3/#securing-spring-boot-admin)
- 보안 기능 자체는 제공하지 않으며 로그인 페이지와 로그아웃 버튼을 제공함
- URL 에 시큐리티 코드 샘플 있음

## 참조
- [Spring Boot Admin](https://codecentric.github.io/spring-boot-admin/2.4.3/)