# Spring DBCP2

## DBCP 란?

- Database Connection Pool 의 약자이다. 
- DB와 커넥션을 맺고 있는 객체를 관리하는 역할을 한다.
- 다양한 커넥션풀 구현체가 있다.
  - Apache Commons DBCP
  - HikariCP(Spring Boot 사용시 default)
  - Tomcat JDBC Connection Pool
  - ...

## Apache Commons DBCP2

- maxTotal 과 maxIdle 은 동일한것이 좋다.
  - 다를 경우 풀에 반납시 maxIdle 이 maxTotal 보다 작을 경우 커넥션을 닫게된다. 

| Parameter     | Description                                                                                     | Default |
|---------------|-------------------------------------------------------------------------------------------------|---------|
| initialSize   | 최초 커넥션을 맺을 때 Connection Pool 에 생성되는 커넥션의 개수                                                     | 0       |
| maxTotal      | 동시에 사용할 수 있는 최대 커넥션의 개수                                                                         | 8       |
| minIdle       | 최소한으로 유지할 커넥션의 개수                                                                               | 0       |
| maxIdle       | Connection Pool 에 반납할 때 최대로 유지될 수 있는 커넥션의 개수                                                    | 8       |
| maxWaitMillis | Pool 이 예외를 throw 하기 전 연결이 반환될 때까지(사용 가능한 Connection 객체가 없는경우) <br/>대기하는 최대 시간(ms) 또는 무한정 대기(-1) | -1      |

### 커넥션 검증 관련

| Parameter       | Description                                                                                         | Default |
|-----------------|-----------------------------------------------------------------------------------------------------|---------|
| validationQuery | 커넥션 풀에서 연결의 유효성을 검사하는데 사용할 SQL 쿼리를 지정한다.<br/>Oracle: select 1 from dual<br/>MySQL, MS-SQL: select 1 | -       |
| testOnCreate    | 커넥션 생성시 실행                                                                                          | false   |
| testOnBorrow    | 커넥션 풀에서 커넥션을 얻어올 때 실행                                                                               | true    |
| testOnReturn    | 커넥션 풀로 커넥션을 반환할 때 실행                                                                                | false   |
| testWhileIdle   | Evictor 가 실행될 때 커넥션 풀 안에 있는 유휴 상태의 커넥션을 대상으로 실행                                                     | false   |

### Evictor 스레드 관련 속성

- Evictor 스레드는 Commons DBCP 내부에서 커넥션 자원을 정리하는 구성 요소이며 별도의 스레드로 실행된다.

| Parameter                       | Description                                                                    | Default   |
|---------------------------------|--------------------------------------------------------------------------------|-----------|
| timeBetweenEvictionRunsMillis   | Evictor 스레드가 동작하는 간격                                                           | -1 (비활성화) |
| numTestsPerEvictionRun          | Evictor 동작 시 한 번에 검사할 커넥션의 개수                                                  | 3         |
| minEvictableIdleTimeMillis      | Evictor 동작 시 커넥션의 유휴 시간을 확인해 설정 값 이상일 경우 커넥션을 제거<br/>-1 로 설정할 경우 사용하지 않음. (권장) | 30분       |
| softMiniEvictableIdleTimeMillis | Evictor 가 커넥션을 제거하기 전에 minIdle 수 만큼의 커넥션은 남기도록 한다. <br/>이때 설정값 시간만큼 존재한다.      | -1        |

### Connection Leak

오랫동안 열려만 있고 `Connection.close()` 메서드가 호출되지 않는 커넥션을 임의로 닫는 기능을 설정하는 옵션이다.

```yaml
spring:
  datasource:
    dbcp2:
      remove-abandoned-on-borrow: true
      remove-abandoned-on-maintenance: true
      remove-abandoned-timeout: 60
      log-abandoned: true
```

## 참조

- [Docs, dbcp2](https://commons.apache.org/proper/commons-dbcp/configuration.html)
- [Commons DBCP 이해하기](https://d2.naver.com/helloworld/5102792)
