# Spring Cloud Config Server

개발환경
- spring boot 2.6.7
- Java 8
- Maven

pom.xml
```xml
<properties>
  <java.version>1.8</java.version>
  <spring-cloud.version>2021.0.1</spring-cloud.version>
</properties>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>${spring-cloud.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
Java Config
```java
@SpringBootApplication
@EnableConfigServer   // Cloud Config Server 설정
public class SpringCloudConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudConfigServerApplication.class, args);
  }
}
```
application.yml
```yaml
server:
  port: 8888

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: "*"

spring:
  application:
    name: config-server
  profiles:
    active: native    # File System 의 config 를 사용
```

## HTTP 호출을 통한 리소스 형식
`EnvironmentController` 에서 처리
- application : `spring.application.name`
- profile : `spring.profiles.active`
- label : Git 을 사용할 경우 branch (default: master)
```text
GET /{application}/{profile}[/{label}]
GET /{application}-{profile}.yml
GET /{label}/{application}-{profile}.yml
GET /{application}-{profile}.properties
GET /{label}/{application}-{profile}.properties
```

## Config Server 에서 설정 정보 가져오는 시점
- application 이 올라 갈때 가져오는것이 아닌 호출시마다 갱신해서 가져옴
- client 측에서 EnvironmentController 의 설정 정보를 가져오는 api 호출시에  
  EnvironmentRepository.findOne 을 호출하며 그시점에 가져온다.

## [Environment Repository](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_environment_repository)
- Git (GitRepositoryConfiguration)
  - default 설정임, `@Profile("git")` 
  - github 사용시 suffix `.git` 필수아님.
  - gitlab 사용시에 suffix `.git` 필수임. 없으면 _422 Unprocessable Entity_ 발생 
```yaml
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://gitlab.com/csh0034/spring-cloud-config-repo.git
```
- File System (NativeRepositoryConfiguration)
  - `@Profile("native")`
  - search-locations 설정 안할 경우 검색 위치
    - "optional:classpath:/"
    - "optional:classpath:/config/"
    - "optional:file:./"
    - "optional:file:./config/"
```yaml
spring:
  application:
    name: config-server
  profiles:
    active: native
#  cloud:
#    config:
#      server:
#        native:
#          search-locations: classpath:/sample-config/
```
- Redis
- Database
- ...

## [Encryption and Decryption](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_encryption_and_decryption)

### asymmetric cryptography (비대칭 키 방식)

Key Store 생성

```shell
$ keytool -genkeypair -alias config-server-key -keyalg RSA \
  -dname "CN=Config Server,OU=Spring,O=ASk" \
  -keypass ask112 -keystore config-server.jks -storepass ask112
```

Properties Setting

```yaml
encrypt:
  key-store:
    location: classpath:config-server.jks
    alias: config-server-key
    password: ask112
    secret: ask112
```

### 암호화된 프로퍼티 설정

암호화된 프로퍼티의 경우 `{cipher}` 를 prefix 로 추가해야한다.  
properties 파일에선 암호화한 값을 따옴표로 감싸면 안된다. yml 은 필요함.

```yaml
profile:
  name: api-default
  password: '{cipher}AQAyKGQ1C3N7051C6bvf2udjx403v5pa8944wKsFr1JzkEnL6aWVdnUx+z4MasnTORakG1QdaPgCJ5elSq8srvT97iinnNlcyjOFmdCQdWuj1ocNZHW9XuZ2uDHzDCBpMGCW9WsutrchkgvXLAaYxe+43Fr4c/Mk3yM4RwLbizc+KFHUo3bsXm9uSyPMc4dJucxE3zc+YVhmdGEnhck3Dt/EdyHrqU/15adHh6xW2qxKUHa/m2+pLUg8WMx469RgaeULqD3DBAPUqPkuERcrdGtbpFsTHfCANC+o1wjDhxCe6TTw+fFa5fk7Xfmw4fG59oCb8LoL87detDOK83I6H56ASYCWew27NcedcP0iU9v8soadr4UWQ2SU9b5oYjpsjcU='
```

서버는 `/encrypt` 와 `/decrypt` 엔드포인트를 제공한다.  
데이터 특수문자가 있을 경우 `-d` 대신 `--data-urlencode` 를 사용하거나 `Content-Type: text/plain` 를 명시해야한다.

```shell
$ curl localhost:8888/encrypt -d 1234
AQCK3hWnMyz6inUZymXeNljnEPO8l4NI+4cDgTcaETKnLeReUvVF2o3NAaMiGCezXJSIrbMG3crs2J+mwgA5bWPp0CSm9rGRtB3zXVY8MYWwS42I0/Jr8IUWmRKyXdZJIkrU1GzIWqmT6EXQGH2w3qoYLeEWADPikBbnvD6E/fIWwJnqP8RlOUOpCJ4CL4vQVjHwyBmb4yUL/e2kw99HRpQXB8KkMWfKlg4+wrhMq4pJdGZfhYLVb4lArEolBH4a1owIQXanG+0e6rOh/ClTVQ2T3HP6bUsyPlZt38q4kiHjR9WZr5XgEbE+i/WyqwXrUwGL0SCgV8IkyLj1KYQf/Ug2al3RcnV6GAJLQ06MzxDUupcqbgupxLQ1eAoQB54v6Zw=

$ curl localhost:8888/decrypt -d AQBfRIKEbdIM4i2JzMgJzzR6qQIWU7zbRHMQresphgqAHG5s3OShwdeCpop6fNWDc1geqCqFppfLi979mBYmfFcqj0FfCIk0cEaV7FnFZO9lyu0GpQXYjLGjbfxMZGZ+b72XCheItxOi7QN2c8wsM/i72GwmODyBOH4A9wBWOFlHkBAgNmUKTnKY2r45FNJlmruSC8uQy2WmFHLlKPeDBrOhNvjttgYhyylQdnOtowDe4DsImFK37bU4EKXdy3EMV5wC+F5bLsQjeZ9Mrlk951901B4X4cjUD0//PzqIZaaVb8QpbaRD9FAiCW+GvAHx6VUUj2z6CGmsDhiov+sjr6X051Zp58GUP88JwSSRIlVcdUyU1rvV14mMXzb59qflltM=
1234
```

> 맨마지막에 `%` 가 붙는다면 이는 zsh 의 줄바꿈 프린트 기능이므로 제외해야함.

### 암호화된 속성 제공

설정에 대해서 복호화를 Config 서버가 아닌 클라이언트에서 수행해야 할 경우 하단 프로퍼티 설정

```properties
spring.cloud.config.server.encrypt.enabled=false
```

```yaml
spring:
  cloud:
    config:
      server:
        encrypt:
          enabled: false
```

## 참조

- [spring-cloud-configuration, baeldung](https://www.baeldung.com/spring-cloud-configuration)
