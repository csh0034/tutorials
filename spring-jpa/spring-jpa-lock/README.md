# spring-jpa-lock

## 스프링 부트 + JPA LOCK
개발환경
- IntelliJ IDEA 2021.2.1
- spring boot 2.5.5
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
<!--test embedded db -->
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

application.yml
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        show_sql: false
        format_sql: true
    open-in-view: false

logging:
  level:
    "[org.hibernate.SQL]": debug
    "[org.hibernate.type.descriptor.sql]": trace
```

***
## 낙관적잠금(Optimistic Lock)
트랜잭션 대부분은 충돌이 발생하지 않는다고 낙관적으로 가정하는 방법
- 데이터베이스가 제공하는 락 기능을 사용하는것이 아니라 JPA가 제공하는 버전 관리 기능을 사용한다. 
- 애플리케이션이 제공하는 락이다
- 트랜잭션을 커밋하기 전까지는 트랜잭션의 충돌을 알 수 없다
- 버전정보를 이용하여 업데이트를 처리하는 방법
- 버전정보로 사용할 컬럼에 @Version 어노테이션을 부여해야함
- Lock 보다는 충돌 감지에 가깝다

### @Version
- 사용 가능 타입 : long, Long, int, Integer, short, Short, TimeStamp
- 각 엔티티 클래스에는 하나의 버전 속성 만 있어야 한다
- @Version 어노테이션을 붙이면 엔티티가 수정될때 자동으로 버전이 하나씩 증가하며,   
  수정할때 조회 시점의 버전과 다를 경우 예외가 발생
  - `javax.persistence.OptimisticLockException` in JPA
  - `org.hibernate.StaleObjectStateException` in Hibernate
  - `org.springframework.orm.ObjectOptimisticLockingFailureException` in Spring

### 낙관적 잠금의 LockModeType
**NONE**  
별도의 옵션을 사용하지 않아도 Entity에 @Version이 적용된 필드만 있으면 낙관적 잠금이 적용 

**OPTIMISTIC (Read)**  
Entity 수정시에만 발생하는 낙관적 잠금이 읽기 시에도 발생하도록 설정  
읽기시에도 버전을 체크하고 트랜잭션이 종료될 때까지 다른 트랜잭션에서 변경하지 않음을 보장  
트랜잭션 커밋 시 버전정보를 조회(SELECT)해 Entity의 버전과 같은지 확인  
이를 통해 dirty read와 non-repeatable read를 방지
>JPA(Hibernate)에서는 자식 엔터티만 수정할 경우 부모엔터티는 변경이 있다고 판정되지 않는다.

**OPTIMISTIC_FORCE_INCREMENT (Write)**  
낙관적 잠금을 사용하면서 버전 정보를 강제로 증가시키는 옵션  
Entity를 수정하지 않아도 트랜잭션 커밋 시 버전정보를 강제로 증가  
Entity 수정 시 버전 UPDATE가 발생하는데, 트랜잭션 커밋 시점에도 버전정보가 증가하므로 2번의 버전 증가가 발생할 수 있다.
> 연관관계가 있는 Entity들에 대해 연관관계의 주인인 Entity의 변경이 발생하면 연관관계에 있는 Entity의 버전정보도 동일하게 변경

**READ, WRITE**  
READ == OPTIMISTIC    
WRITE == OPTIMISTIC_FORCE_INCREMENT    
JPA 1.0의 호환성을 유지하기 위해서 존재하는 옵션

***
## 비관적잠금(Pessimistic Lock)
트랜잭션의 충돌이 발생한다고 가정하고 우선적으로 락을 거는 방법
- 데이터베이스가 제공하는 락기능을 사용
- 대표적으로 select for update 구문이 있다
- Pessimistic Lock은 공유 락(Shared Lock) 과 배타적 락(Exclusive Lock) 두가지 타입이 있다
  - Shared Lock : 다른 사용자가 동시에 데이터를 읽을 수는 있지만 Write는 할 수 없다
  - Exclusive Lock : 데이터를 변경하고자 할 때 사용되며, 트랜잭션이 완료될 때까지 유지되어 해당 Lock이  
    해제될 때까지 다른 트랜잭션은 해당 데이터에 읽기를 포함하여 접근을 할 수 없다

### 비관적 잠금의 LockModeType
**PESSIMISTIC_READ**  
Shared Lock을 얻고 데이터가 업데이트되거나 삭제되지 않도록 한다  
select ... for share

**PESSIMISTIC_WRITE**  
Exclusive Lock을 획득하고 데이터를 읽거나, 업데이트하거나, 삭제하는 것을 방지  
select ... for update

**PESSIMISTIC_FORCE_INCREMENT**  
PESSIMISTIC_WRITE와 유사하게 작동하며 엔티티의 버전 속성을 추가로 증가시킨다  
for update nowait