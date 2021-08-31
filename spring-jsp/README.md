# spring jsp

## spring boot + jsp
개발환경
- IntelliJ IDEA 2021.1
- spring boot 2.5.3
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
  <artifactId>spring-boot-starter-tomcat</artifactId>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>org.apache.tomcat.embed</groupId>
  <artifactId>tomcat-embed-jasper</artifactId>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>javax.servlet</groupId>
 <artifactId>jstl</artifactId>
 <version>1.2</version>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
```
Java Config
```java
@SpringBootApplication
public class SpringJspApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(SpringJspApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(SpringJspApplication.class);
  }
}
```
application.yml
```yml
spring:
  mvc:
    view:
      prefix: /WEB-INF/jsp/
      suffix: .jsp
```
프로젝트 구조  
![01](./images/01.png)

## 참고사항
> 만약 멀티 모듈 구성일 경우 인텔리제이 Edit Configuration 선택후  
> Working directory $MODULE_WORKING_DIR$ 추가 해야 jsp 파일 404 오류 발생하지 않음

> jsp 파일 변경시에 리로드 하지 않고 반영하기 위해선  
> spring-boot-devtools 의존성이 있어야함