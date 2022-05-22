# Spring ShedLock

```xml
<properties>
  <shedlock.version>4.35.0</shedlock.version>
</properties>

<!-- spring 과 통합 지원 -->
<dependency>
  <groupId>net.javacrumbs.shedlock</groupId>
  <artifactId>shedlock-spring</artifactId>
  <version>${shedlock.version}</version>
</dependency>

<!-- redis 와 통합 지원 -->
<dependency>
  <groupId>net.javacrumbs.shedlock</groupId>
  <artifactId>shedlock-provider-redis-spring</artifactId>
  <version>${shedlock.version}</version>
</dependency>
```

## ShedLock 이란 ?

예약된 작업이 동시에 최대 한 번만 실행되도록 한다.  
한 노드에서 작업이 이미 실행 되고 있을 경우 다른노드에서 실행을 기다리지 않고 건너뛴다.

> ShedLock 은 완전한 분산 스케줄러가 아니며 잠금 역할만 한다.

### Components

- Core: 잠금 메커니즘
- Integration: Spring AOP, Micronaut AOP 또는 manual code 를 사용하여 애플리케이션과 통합
- Lock provider:  SQL DB, Mongo, Redis 등과 같은 외부 프로세스를 사용하여 잠금을 제공

### Lock Providers 

Lock 정보를 저장하기 위하여 다양한 LockProvider 구현체를 제공한다.

- JdbcTemplate
- R2DBC
- Micronaut Data Jdbc
- Mongo
- DynamoDB
- DynamoDB 2
- ZooKeeper (using Curator)
- Redis (using Spring RedisConnectionFactory)
- Redis (using Jedis)
- ...

### SchedulerLock 의 속성

- name: 잠금의 이름, 같은 이름을 가지면 동시에 하나의 작업만 실행됨
- lockAtLeastFor: 잠금이 유지되어야 하는 최소 기간
- lockAtMostFor: 잠금을 해제하기 전에 잠금을 획득한 기계가 죽었을 경우 잠금을 유지해야 하는 기간.

> 일반적으로 작업이 15분 마다 실행될 경우 lockAtLeastFor, lockAtMostFor 를 14분으로 해놓으면 된다.

## Configuration

### Redis 와 통합

```java
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class ShedLockConfig {

  @Bean
  public LockProvider lockProvider(RedisConnectionFactory redisConnectionFactory) {
    return new RedisLockProvider(redisConnectionFactory, System.getProperty("lock.env", "default"));
  }

  @Bean
  public LockingTaskExecutor lockingTaskExecutor(LockProvider lockProvider) {
    return new DefaultLockingTaskExecutor(lockProvider);
  }

}
```

### 로그 출력

```yaml
logging:
  level:
    "[net.javacrumbs.shedlock]": debug
```

### @Scheduled 사용

```java
@Component
@Slf4j
public class SampleSchedule {

  @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
  @SchedulerLock(name = "sampleSchedule", lockAtLeastFor = "9s", lockAtMostFor = "9s")
  public void scheduledTask() throws InterruptedException {
    log.info("sampleSchedule start....");
    Thread.sleep(5000);
    log.info("sampleSchedule done....");
  }
```

### Locking without a framework

```java
@Component
@Slf4j
@RequiredArgsConstructor
public class LockRunner implements ApplicationRunner {

  private final LockingTaskExecutor lockingTaskExecutor;

  @Override
  public void run(ApplicationArguments args) {
    String lockKey = DigestUtils.sha1Hex("key..");

    lockingTaskExecutor.executeWithLock((Runnable) () -> {
      log.info("LockRunner start...");
      
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      
      log.info("LockRunner end...");
    }, new LockConfiguration(Instant.now(), lockKey, Duration.ofSeconds(10), Duration.ofSeconds(10)));
  }

}
```

## 참조

- [GitHub, ShedLock](https://github.com/lukas-krecan/ShedLock#redis-using-spring-redisconnectionfactory)
