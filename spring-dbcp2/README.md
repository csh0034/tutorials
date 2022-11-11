# Spring DBCP2

## Connection Leak

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
