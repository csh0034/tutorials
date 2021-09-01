# spring dbunit

## spring boot + dbunit
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
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.mariadb.jdbc</groupId>
  <artifactId>mariadb-java-client</artifactId>
</dependency>
<dependency>
  <groupId>org.dbunit</groupId>
  <artifactId>dbunit</artifactId>
  <version>2.7.0</version>
  <scope>test</scope>
</dependency>
```

---
##참고 사항
- Intellij DbUnit Extractor 플러그인 설치

---
##ref
- [dbunit docs](http://dbunit.sourceforge.net/components.html)
- [스프링부트에서 DbUnit 을 이용하여 DB 테스트 해보기](https://techblog.woowahan.com/2650)
