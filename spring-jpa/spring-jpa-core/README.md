# spring jpa core

## Slow Query Log
`org.hibernate.engine.jdbc.internal.ResultSetReturnImplHibernate` 에서 처리

1. Slow Query 임계값 지정
2. Slow Query 대상 로그 출력

```yaml
spring:
  jpa:
    properties:
      hibernate:
        generate_statistics: true
        "[session.events.log.LOG_QUERIES_SLOWER_THAN_MS]": 3000

logging:
  level:
    "[org.hibernate.SQL_SLOW]": info
```

`generate_statistics`
- Hibernate 통계정보를 출력

`hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS`
- Slow Query 임계값을 밀리세컨드로 지정한다.

`org.hibernate.SQL_SLOW=info`
- Slow Query 로그를 출력한다.

> 임계값은 쿼리 실행 전후의 준비 및 결과 처리 단계는 포함하지 않는다.  
> 즉, 데이터베이스의 **순수 쿼리 실행 시간**만 측정하여 비교한다.

### 임계값 1로 세팅한 후 쿼리 발생시 로그
```text
# Slow Query
SlowQuery: 4 milliseconds. SQL: 'prep1: insert into tb_user (name, password, user_id) values (?, ?, ?) {1: 'ask', 2: '1234', 3: 'a87b11ce-66a4-4b2b-a059-8bb293cbe70f'}'

# Statistics
Session Metrics {
    556084 nanoseconds spent acquiring 1 JDBC connections;
    0 nanoseconds spent releasing 0 JDBC connections;
    1174847 nanoseconds spent preparing 1 JDBC statements;
    4415805 nanoseconds spent executing 1 JDBC statements;
    0 nanoseconds spent executing 0 JDBC batches;
    0 nanoseconds spent performing 0 L2C puts;
    0 nanoseconds spent performing 0 L2C hits;
    0 nanoseconds spent performing 0 L2C misses;
    45097233 nanoseconds spent executing 1 flushes (flushing a total of 1 entities and 0 collections);
    0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)
}
```

## 참조
- [hibernate-slow-query-log](https://vladmihalcea.com/hibernate-slow-query-log/)
