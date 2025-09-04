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

### 참조
- [hibernate-slow-query-log](https://vladmihalcea.com/hibernate-slow-query-log/)

## Hibernate Validator

Entity 에 JSR303, JSR349, JSR 380 관련 어노테이션을 추가하면  
DDL 생성시에 validation 어노테이션을 읽어서 스키마 생성시 적용한다.  
또한 pre-persist, pre-update, pre-remove 시점에 validation 이 동작한다.   

### Schema 에 적용

Entity 에 javax.validation annotation (ex. @NotNull) 이 있을 경우 스키마 생성 시점에 동작한다.    
- ex) @NotNull 추가시 nullable false 로 동작함
- `org.hibernate.cfg.beanvalidation.BeanValidationIntegrator` 에서 처리됨
- [추가 제약 사항 적용 설명](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-builtin-constraints)

### pre-persist, pre-update, pre-remove 시점에 적용

- `org.hibernate.cfg.beanvalidation.BeanValidationEventListener` 에서 처리됨

트랜잭션 안에서 validation 에 실패하여 ConstraintViolationException 이 발생한 경우  
RuntimeException 의 하위 클래스이므로 TransactionSystemException 으로 처리되며 롤백된다.

## spring.jpa.hibernate.use-new-id-generator-mappings

- AUTO, TABLE 및 SEQUENCE 에 대해 Hibernate 의 새로운 IdentifierGenerator 를 사용할지 여부
- `hibernate.id.new_generator_mappings` 프로퍼티
- spring boot 2.0 버전 기준 기본값은 `true` 

### MySql(MariaDB) 에서  `GenerationType.AUTO` 를 사용시

- 해당값이 true 일 경우 `GenerationType.TABLE`  
- 해당값이 false 일 경우  `GenerationType.IDENTITY`

### MySql(MariaDB) 에서  `GenerationType.TABLE` 를 사용시

- 해당값이 true 일 경우 `TableGenerator` 를 사용
- 해당값이 false 일 경우 deprecated 된 `MultipleHiLoPerTableGenerator` 를 사용

## Hibernate Primary Key (ID) Generator

- `DefaultIdentifierGeneratorFactory`
- [참고 링크](https://kwonnam.pe.kr/wiki/java/hibernate/id_generator)

## Replication Datasource

데이터베이스 부하분산을 위해 Write 작업은 Master DB 에서, Read 작업은 Slave DB 에서 처리하도록 하는 방법  
`AbstractRoutingDataSource` 를 상속받아 구현하며 ReadOnly 트랜잭션인 경우 Slave DB 에서 처리하도록 한다.

### Replication 주의점

Replication 을 적용하면 Master/Slave 서버 간 데이터 동기화까지의 시간이 소요되기 때문에 데이터 정합성 문제가 발생할 수 있다.

[정합성 관련 정리 블로그](https://da-nyee.github.io/posts/db-replication-data-consistency-issue/)

## OSIV(Open Session In View)

OSIV 사용시 트랜잭션 범위 밖에서도 세션을 유지하므로 하단의 경우 변경감지가 동작할 수 있다.

1. 트랜잭션 내부에서 엔티티 조회 후 트랜잭션 종료
2. 엔티티 변경
3. 별도 트랜잭션 시작후 종료 후 > flush 호출
4. 1번에서 조회된 엔티티 변경감지 동작

## PostActionEventListener 와 TransactionSynchronizationManager.registerSynchronization 호출 순서

1. PostActionEventListener
    - hibernate 코드이며 JpaTransactionManager.doCommit 에서 tx.commit 시점에 처리됨
2. TransactionSynchronizationManager.registerSynchronization
    - spring tx 코드이며 JpaTransactionManager.doCommit 이후에 triggerAfterCommit 에서 처리됨

### 참조
- [Hibernate Validator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/)
- [validate entities with hibernate validator](https://thorben-janssen.com/automatically-validate-entities-with-hibernate-validator/)
- [hibernate-notnull-vs-nullable, baeldung](https://www.baeldung.com/hibernate-notnull-vs-nullable)
