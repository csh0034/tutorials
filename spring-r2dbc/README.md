# Spring r2dbc

## 개발환경
- spring boot 2.6.3
- MariaDB 10.4.20

### pom.xml

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>
<dependency>
  <groupId>org.mariadb</groupId>
  <artifactId>r2dbc-mariadb</artifactId>
  <scope>runtime</scope>
</dependency>
```

### application.yml

```yaml
spring:
  r2dbc:
    url: r2dbc:mariadb://127.0.0.1:3306/db_r2dbc
    username: root
    password: 111111

logging:
  level:
    "[org.springframework.r2dbc]": debug
```

## R2DBC 란?
Reactive Relational Database Connectivity 의 줄임말로서 관계형 데이터베이스 접근을 위한 리액티브 API 이다.  
즉, 적은수의 스레드로 동시성을 처리하며 적은 리소스로 확장가능한 not-blocking 스택이다.

### [R2DBC Drivers](https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/#reference)

Spring Data R2DBC 는 ConnectionFactory 를 통하여 Database Dialect 를 선택한다.  
사용하는 드라이버가 아직 등록된 상태가 아니라면 자체 `R2dbcDialect` 를 설정해야한다.

- H2 (io.r2dbc:r2dbc-h2)
- MariaDB (org.mariadb:r2dbc-mariadb)
- Microsoft SQL Server (io.r2dbc:r2dbc-mssql)
- MySQL (dev.miku:r2dbc-mysql)
- jasync-sql MySQL (com.github.jasync-sql:jasync-r2dbc-mysql)
- Postgres (io.r2dbc:r2dbc-postgresql)
- Oracle (com.oracle.database.r2dbc:oracle-r2dbc)

## Troubleshooting

### 새로운 모델 저장시 이슈

R2DBC Repository 는 모델 저장시에 id 필드의 값이 있을 경우 Update 로 동작하여 해당 모델이 DB에 없을 경우 예외가 발생한다.

![01.png](images/01.png)

repository.save 호출시 새로운 객체일 경우에만 insert 를 호출 한다.

해당 대상이 `org.springframework.data.domain.Persistable` 를 구현 한 경우 이를 호출하여 판단하거나 

`org.springframework.data.mapping.model.PersistentEntityIsNewStrategy` 에서 아래와 같이 판단한다.

![02.png](images/02.png)

따라서 DB 에 Auto Increment 등을 세팅하지 않고 직접 할당 해야 할 경우에는 Persistable 구현후에    
id 필드가 null 일때 할당과 동시에 true 를 반환하면 된다.

```java
@org.springframework.data.relational.core.mapping.Table("mt_user")
@Data
public class User implements Persistable<String> {

  @Id
  private String id;

  private String name;

  private Integer age;

  @org.springframework.data.relational.core.mapping.Column("created_dt")
  @org.springframework.data.annotation.CreatedDate
  private LocalDateTime createdDt;

  @org.springframework.data.relational.core.mapping.Column("created_by")
  @org.springframework.data.annotation.CreatedBy
  private String createdBy;

  public static User create(String name, Integer age) {
    User user = new User();
    user.name = name;
    user.age = age;
    return user;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    if (id == null) {
      id = UUID.randomUUID().toString();
      return true;
    }
    return false;
  }

}
```

#### 이슈 1

이 방법으로 하면 Model 에 @CreatedDate 를 추가하면 문제가 생긴다.  
`ReactiveIsNewAwareAuditingHandler` 에서 Auditing 수행할때도 isNew 를 호출한다.  
따라서 미리 isNew 가 호출되어 id 가 채워져있으면 무조건 Modified 로만 동작됨

![03.png](images/03.png)

#### 해결

Auditing 기능에 의해 createdDt 가 들어가기 자동으로 때문에 createdDt 가 null 이면 새로운 객체로 처리하면됨

```java
@Override
@JsonIgnore
public boolean isNew() {
  if (createdDt == null) {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    return true;
  }
  return false;
}
```

### 테스트에서 Transaction rollback 안되는 이슈

- [GitHub Issue](https://github.com/spring-projects/spring-framework/issues/24226)

@Transactional 을 통한 리액터 테스트시에 전파 방법이 부족하다.

동기식일 경우 트랜잭션은 ThreadLocal 에 저장되지만 비동기식일 경우엔 Context 에 저장된다.  
트랜잭션 상태를 적용하기위해선 Publisher 를 리턴해야하는데 테스트 메서드에선 void 를 리턴하므로 이를 처리할 수 없다.

또한, Assertions 를 사용 할 경우 StepVerifier 를 사용하는데 이는 블록킹을 발생시킨다.  
Assertions 는 Publisher 를 Subscribe 하여 처리하므로 외부에서 블록킹 방식으로 트랜잭션을 처리가 안된다. 

따라서 트랜잭션 처리가 필요 할 경우 테스트 마다 StepVerifier 에 트랜잭션 롤백 기능을 추가해야 한다.

```java
@Component
public class Transaction {

  private static TransactionalOperator rxtx;

  @Autowired
  public Transaction(final TransactionalOperator rxtx) {
    Transaction.rxtx = rxtx;
  }

  public static <T> Mono<T> withRollback(final Mono<T> publisher) {
    return rxtx.execute(tx -> {
          tx.setRollbackOnly();
          return publisher;
        })
        .next();
  }

  public static <T> Flux<T> withRollback(final Flux<T> publisher) {
    return rxtx.execute(tx -> {
      tx.setRollbackOnly();
      return publisher;
    });
  }

}
```

```java
userRepository.save(User.create(name, age))
    .as(Transaction::withRollback)
    .as(StepVerifier::create)
    .assertNext(user -> {
      log.info("user : {}", user);
      assertAll(
          () -> assertThat(user.getName()).isEqualTo(name),
          () -> assertThat(user.getAge()).isEqualTo(age)
      );
    })
    .verifyComplete();
```

## 참조
- [Spring-Data-r2dbc](https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html)
- [Unblock Your Applications with R2DBC, Spring Data and MariaDB](https://mariadb.com/ko/resources/blog/unblock-your-applications-with-r2dbc-spring-data-and-mariadb/)
- [reactive-transactions-with-spring](https://spring.io/blog/2019/05/16/reactive-transactions-with-spring)
