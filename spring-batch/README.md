# spring batch
## 스프링 부트 + 스프링 배치 시작하기
maven
```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
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
java
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
    driver-class-name: org.mariadb.jdbc.Driver
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
## Tasklet
...  


## ItemReader
...

## ItemProcessor
**ItemProcessor** 는 필수가 아니며 가공 (혹은 처리) 단계임  
Reader, Writer 와는 별도의 단계로 분리되었기 때문에 **비지니스 코드가 섞이는 것을 방지**

## ItemWriter
...

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
  Spring Batch는 같은 JobParameter로 같은 Job을 두 번 실행하지 않는다
  ```java
  @Value("#{jobParameters[file]}")
  private String file;
  ```
  Scope Bean을 생성할때만 가능


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
- [MySQL 환경의 스프링부트에 하이버네이트 배치 설정 해보기](https://techblog.woowahan.com/2695/)