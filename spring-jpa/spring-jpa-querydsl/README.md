# spring-jpa-querydsl

## querydsl 설정

- querydsl-apt 를 plugin 에 dependency 로 추가해도 됨

```xml
<dependencies>
    <dependency>
      <groupId>com.querydsl</groupId>
      <artifactId>querydsl-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>com.querydsl</groupId>
      <artifactId>querydsl-apt</artifactId>
      <scope>provided</scope>
    </dependency>
</dependencies>

<plugins>
  <plugin>
    <groupId>com.mysema.maven</groupId>
    <artifactId>apt-maven-plugin</artifactId>
    <version>1.1.3</version>
    <executions>
      <execution>
        <goals>
          <goal>process</goal>
        </goals>
        <configuration>
          <outputDirectory>target/generated-sources/java</outputDirectory>
          <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
        </configuration>
      </execution>
    </executions>
  </plugin>
</plugins>
```

## querydsl deadlock 방지

- Multithreaded initialization of Querydsl Q-types
- QClass 가 멀티쓰레드에서 초기화 될때 순환 종속성이 존재할경우 deadlock 이 발생할 수 있다.
- 하단 코드를 추가하여 여러 쓰레드에서 사용되기 전에 싱글쓰레드에서 미리 초기화를 시킬수 있다. 

```java
ClassPathUtils.scanPackage(Thread.currentThread().getContextClassLoader(), DOMAIN_PACKAGE);
```

## 참조

- [GitHub, querydsl](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa)
- [Reference, querydsl](http://querydsl.com/static/querydsl/5.0.0/reference/html_single/)
