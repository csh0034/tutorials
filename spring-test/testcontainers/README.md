# Testcontainers

## Testcontainers 란?

JUnit 테스트를 지원하며 테스트에서 도커 컨테이너를 실행할 수 있는 라이브러리이다.

```xml
<properties>
  <testcontainers.version>1.17.2</testcontainers.version>
</properties>
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers-bom</artifactId>
      <version>${testcontainers.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>mariadb</artifactId>
  <scope>test</scope>
</dependency>
```

## Testcontainers 기능

### Annotation

#### @Testcontainers

JUnit5 확장팩으로 테스트 클래스에 @Container 를 사용한 필드를 찾아서 컨테이너 라이프사이클  
관련 메소드를 실행해준다. 

#### @Container

인스턴스 변수 사용하면 모든 테스트 마다 컨테이너를 재시작 하고, static 변수에 사용하면 클래스  
내부 모든 테스트에서 동일한 컨테이너를 재사용한다.

### 지원 기능

#### 컨테이너 만들기

- GenericContainer(String imageName)
- DockerComposeContainer
- 지원 라이브러리 의존성이 있을 경우 MariaDBContainer 와 같이 사용가능

#### 네트워크

- withExposedPorts(int...)
- getMappedPort(int)

#### 환경 변수 설정

- withEnv(key, value)

#### 명령어 실행

- withCommand(String cmd...)

#### 사용할 준비가 됐는지 확인하기

- waitingFor(Wait)
- Wait.forHttp(String url)
- Wait.forLogMessage(String message)

#### 로그 살펴보기

- getLogs()
- followOutput()

## Docker-compose 사용

- 컨테이너 포트만 입력해두고 테스트시에 가용한 포트를 노출시키도록 처리
- `@DynamicPropertySource` 를 사용하여 노출된 호스트 포트를 env 에 등록

```yaml
version: '3.2'

services:
  mariadb:
    image: mariadb:10.4
    environment:
      MARIADB_DATABASE: 'test_db'
      MARIADB_ROOT_HOST: '%'
      MARIADB_ROOT_PASSWORD: '1234'
      LANG: C.UTF-8
#      TZ: Asia/Seoul
    ports:
      - '3306'
    command:
      - '--character-set-server=utf8mb4'
      - '--collation-server=utf8mb4_unicode_ci'
      - '--skip-character-set-client-handshake'
      - '--lower_case_table_names=1'
```

```java
@SpringBootTest
@Testcontainers
@ActiveProfiles({"test", "compose"})
@Slf4j
class DockerComposeMariaDBContainerTest {

  @Container
  static DockerComposeContainer<?> container = new DockerComposeContainer<>(
      new File("src/test/resources/mariadb/docker-compose.yml"))
      .withExposedService("mariadb_1", 3306);

  // 테스트 코드...

  @DynamicPropertySource
  static void mariaDBProperties(DynamicPropertyRegistry registry) {
    registry.add("mariadb.port", () -> container.getServicePort("mariadb_1", 3306));
  }

}
```

### application-compose.yml

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:${mariadb.port}/test_db
    username: root
    password: 1234
```

## 참조

- [Reference, Testcontainers](https://www.testcontainers.org/)
- [Use Testcontainers for Integration Testing](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.testing.testcontainers)
- [GitHub, testcontainers-spring-boot](https://github.com/Playtika/testcontainers-spring-boot)
