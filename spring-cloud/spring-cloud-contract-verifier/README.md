# Spring Cloud Contract Verifier

## 정리

하단 플러그인 추가후에 `clean install -DskipTests` 으로 실행하면 `spring-cloud-starter-contract-verifier` 에 대한  
의존성 없이도 stubs.jar 생성가능 

```xml
<plugin>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-contract-maven-plugin</artifactId>
  <extensions>true</extensions>
  <configuration>
    <testFramework>JUNIT5</testFramework>
  </configuration>
</plugin>
```
