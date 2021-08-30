# spring batch
## 스프링 부트 + 스프링 배치 시작하기
개발환경   
- IntelliJ IDEA 2021.1
- spring boot 2.5.3
- Java 8
- Maven
- MariaDB 10.4.20

pom.xml
```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
    <!-- JDBC  -->
    <dependency>
      <groupId>org.mariadb.jdbc</groupId>
      <artifactId>mariadb-java-client</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.batch</groupId>
      <artifactId>spring-batch-test</artifactId>
      <scope>test</scope>
    </dependency>
```
Java Config
```java
@Configuration
@EnableBatchProcessing
public class BatchConfig {
  ...
}
```
application.yml
```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/batch?profileSQL=true
    username: root
    password: 111111
  batch:
    job:
      names: ${job.name:NONE} # Program arguments로 job.name 값이 넘어오면 해당 값과 일치하는 Job만 실행
      enabled: false # 프로젝트 시작시 Job 실행 안함
    jdbc:
      initialize-schema: never  # 메타 데이터 테이블 생성 안함, always:메타 테이블 생성
```

***
## Spring batch 
- 벡엔드의 배치처리 기능을 구현하는 데 사용하는 프레임워크이며 스프링 부트 배치는 스프링 배치 설정   
  요소들을 간편화시켜 스프링 배치를 빠르게 설정하는 데 도움을 준다.
- 사람들이 기대하는 Spring Framework의 특성(생산성, POJO 기반 개발 접근 방식 및 일반적인 사용 용이성)을  
  기반으로 하는 동시에 개발자가 필요할 때 보다 고급 엔터프라이즈 서비스에 쉽게 액세스하고 활용할 수 있도록 한다.  
- 로깅/추적, 트랜잭션 관리, 작업 처리 통계, 작업 재시작, 건너뛰기, 리소스 관리 등   
  대용량 레코드 처리에 필수적인 재사용 가능한 기능을 제공한다.

***
## Spring batch 이해하기
배치의 일반적인 시나리오
- 읽기(read) : 데이터 저장소(일반적으로 데이터베이스)에서 특정 데이터 레코드를 읽습니다.
- 처리(processing) : 원하는 방식으로 데이터 가공/처리 합니다.
- 쓰기(write) : 수정된 데이터를 다시 저장소(데이터베이스)에 저장합니다.

![01](./images/01.png)

Spring Batch에서 Job은 하나의 배치 작업 단위 뜻하며 Job 안에는 아래처럼 여러 Step이 존재하고  
Step 안에 Tasklet 혹은 Reader & Processor & Writer 묶음이 존재한다.  
>Tasklet 하나와 Reader & Processor & Writer 한 묶음이 같은 레벨이기 때문에  
Reader & Processor가 끝나고 Tasklet으로 마무리 하게 만들순 없다

![02](./images/02.png)

Spring Batch 4.0 (Spring Boot 2.0) 에서 지원하는 Reader & Writer  

| DataSource | 기술      | 설명                                       |
|------------|-----------|--------------------------------------------|
| Database   | JDBC      | 페이징, 커서, 일괄 업데이트 등 사용 가능   |
| Database   | Hibernate | 페이징, 커서 사용 가능                 |
| Database   | JPA       | 페이징 사용 가능 (현재 버전에선 커서 없음) |
| File       | Flat file | 지정한 구분자로 파싱 지원              |
| File       | XML       | XML 파싱 지원                      |

***
## Spring Batch 메타 테이블
application.yml
```yaml
spring:
  batch:
    jdbc:
      initialize-schema: always
```

initialize-schema : 메타 테이블 생성 여부
- embedded : h2 등의 embedded db 일 경우 메타 테이블 생성
- always : 항상 생성
- never : 생성 안함

schema 위치 : `classpath:org/springframework/batch/core/schema-@@platform@@.sql`  
> initialize-schema embedded, always 일 경우 현재 연결된 DB에 맞는 메타 테이블 스키마 생성 sql 실행



***
## Tasklet
- **Tasklet**은 Step안에서 단일로 수행될 커스텀한 기능들을 선언할때 사용함

***
## ItemReader
- **ItemReader**는 Step의 대상이 되는 배치 데이터를 읽어오는 인터페이스이다.  
  File, Xml Db등 여러 타입의 데이터를 읽어올 수 있다.

***
## ItemProcessor
- **ItemProcessor**는 필수가 아니며 가공 (혹은 처리) 단계임  
  Reader, Writer 와는 별도의 단계로 분리되었기 때문에 **비지니스 코드가 섞이는 것을 방지**
- 읽어온 배치 데이터와 쓰여질 데이터의 타입이 다를 경우 처리 가능

***
## ItemWriter
- **ItemWriter**는 배치 데이터를 저장하며 일반적으로 DB나 파일에 저장한다.
- **ItemWriter**도 **ItemReader**와 비슷한 방식을 구현합니다. 제네릭으로 원하는 타입을 받고 write()   
  메서드는 List를 사용해서 저장한 타입의 리스트를 매게변수로 받는다.

***
## @JobScope, @StepScope, JobParameter
스프링 bean 의 기본 Scope 는 싱글톤임  
@JobScope, @StepScope 선언시 Bean의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연시킨다


- @JobScope  
  **Step 선언문**에서 사용 가능  
  Job 실행시점에 Bean이 생성되며 Job이 끝나면 삭제됨


- @StepScope
  **Tasklet**이나 **ItemReader**, **ItemWriter**, **ItemProcessor**에서 사용 가능  
  Step 실행시점에 Bean이 생성되며 Step이 끝나면 삭제됨
  

- JobParameter
  Job 실행시 전달된 파라미터 값을 받을 수 있음  
  java -jar *.jar --param=value 로 넣어준다 (Program Arguments)  
  Spring Batch는 같은 JobParameter로 같은 Job을 두 번 실행하지 않는다
  ```java
  @Value("#{jobParameters[file]}")
  private String file;
  ```
  Scope Bean을 생성할때만 가능

***
## MySQL (MariaDB) 배치설정
```yaml
spring:
  jpa:
    properties:
      "[hibernate.jdbc.batch_size]": 100
```
**org.hibernate.SQL 의 쿼리는 단건씩 출력됨**  
- MySQL JDBC의 경우 JDBC URL에 rewriteBatchedStatements=true 옵션을 추가해주면 된다.  
- MySQL의 경우 실제로 생성된 쿼리는 logger=com.mysql.jdbc.log.Slf4JLogger&profileSQL=true 옵션으로 로그를 통해 확인할 수 있다.

```yaml
spring:
  datasource:
    ...
    url: jdbc:mariadb://localhost:3306/batch?rewriteBatchedStatements=true&profileSQL=true&logger=Slf4JLogger&maxQuerySizeToLog=999999
    ...
```
- profileSQL=true : Driver 에서 전송하는 쿼리를 출력
- logger=Slf4JLogger : Driver 에서 쿼리 출력시 사용할 로거를 설정
    - MySQL 드라이버 : 기본값은 System.err 로 출력하도록 설정되어 있기 때문에 **필수**
    - MariaDB 드라이버 : Slf4j 를 이용하여 로그를 출력하기 때문에 설정할 필요가 **없음**
- maxQuerySizeToLog=999999 : 출력할 쿼리 길이
    - MySQL 드라이버 : 기본값 0
    - MariaDB 드라이버 : 기본값 1024

- **MariaDB Driver 의 경우 rewriteBatchedStatements 가 false 일 경우  
useBatchMultiSend 가 true 쿼리를 배치로 실행 하는데 useBatchMultiSend 기본값이 true**
  
- ID 가 자동증가 값 일 경우 (GenerationType.IDENTITY 을 사용할 경우) 하이버네이트는 batch insert 를 비활성화 


## 참조
- [spring-batch-reference](https://docs.spring.io/spring-batch/docs/4.3.x/reference/html)
- [spring batch 간단 정리](https://cheese10yun.github.io/spring-batch-basic/#null)
- [spring-batch-in-action / github](https://github.com/jojoldu/spring-batch-in-action)
- [MySQL 환경의 스프링부트에 하이버네이트 배치 설정 해보기](https://techblog.woowahan.com/2695/)