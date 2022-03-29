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

## 참조

- [querydsl, GitHub](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa)
