# spring quickperf

개발환경
- IntelliJ IDEA 2021.2.3
- spring boot 2.5.6
- Java 8
- Maven
- H2 1.4.200

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
  <groupId>org.quickperf</groupId>
  <artifactId>quick-perf-junit5</artifactId>
  <version>1.1.0</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.quickperf</groupId>
  <artifactId>quick-perf-springboot2-sql-starter</artifactId>
  <version>1.1.0</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

## QuickPerf
- 일부 성능 관련 속성을 빠르게 평가하고 개선하기 위한 Java용 테스트 라이브러리
- `@ExpectSelect` 등을 이용하여 테스트 코드 실행시 쿼리 실행 횟수를 체크 할수 있음

### QuickPerf extension 설정 방법

1. src/test/resources > junit-platform.properties
```properties
junit.jupiter.extensions.autodetection.enabled=true
```
2. 테스트에 해당 어노테이션 추가 (추천)
```java
@ExtendWith(QuickPerfTestExtension.class)
```

### @SpringBootTest 사용시
```java
@SpringBootTest
@ExtendWith(QuickPerfTestExtension.class)
```

### @DataJpaTest 사용시
sol 1. auto-configuration 이 동작 하지 않으므로 QuickPerfSqlConfig 를 Import 해야함 (**추천**)  
sol 2. 강제로 auto-configuration true 로 변경

```java
// sol 1.
@DataJpaTest
@Import(org.quickperf.spring.sql.QuickPerfSqlConfig.class)
@ExtendWith(QuickPerfTestExtension.class)

// sol 2.
@DataJpaTest
@OverrideAutoConfiguration(enabled = true)
@ExtendWith(QuickPerfTestExtension.class)
```

## Test Annotation

### @ExpectSelect
```java
@SpringBootTest
@ExtendWith(QuickPerfTestExtension.class)
@Transactional
@Slf4j
class QuickperfApplicationTests {

  @Autowired
  EntityManager em;

  @ExpectSelect(4)
  @Test
  void findTeam() {
    em.find(Team.class, 1L);
  }
}
```

## 참조
- [quickperf, Github](https://github.com/quick-perf/quickperf)
- [quickperf doc, Github](https://github.com/quick-perf/doc/wiki/Spring)
- [quickperf examples, Github](https://github.com/quick-perf/quickperf-examples)