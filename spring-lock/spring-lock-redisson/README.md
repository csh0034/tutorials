# spring lock redisson

## 스프링 부트 + redisson
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
<dependency>
  <groupId>org.redisson</groupId>
  <artifactId>redisson-spring-boot-starter</artifactId>
  <version>3.16.2</version>
</dependency>
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

  redis:
    host: localhost
    port: 6379

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        show_sql: false
        format_sql: false
    open-in-view: false

logging:
  level:
    "[org.hibernate.SQL]": debug
    "[org.hibernate.type.descriptor.sql.BasicBinder]": trace
```
***
## 동시성과 Distributed Lock 
API 서버들은 대규모 트래픽을 처리하기 위해 여러대의 서버로 구축되어 있는데 평상시엔 걍 분산처리 하면 되지만  
선착순 이벤트등과 같은 공유자원이 있다면 atomic 을 보장 하면서 분산 처리를 해주어야 한다.

트랜잭션이 많이 일어나는 서비스의 경우 동기화된 처리가 필요함(동시성이슈)에 따라서 여러 서버에 공통된 락을 적용시에  
JPA Lock, DB Lock 등 여러 방법이 있지만 이번엔 레디스를 활용하여 분산 락을 활용.

분산락 개발시 문제점
1. Lock의 타임아웃 지정 필요
   -  스핀 락을 구현했을시에 락을 획득하지 못하면 무한 루프를 돌게된다. 
2. 레디스에 많은 부하
   - 스핀 락 사용시 지속적으로 락의 획득을 시도하기 때문에 레디스에 계속 요청을 보내게 되고 이런 트래픽을 처리하느라 부담을 받게 된다.

***
## Redisson 
Redisson은 Jedis, Lettuce 같은 자바 레디스 클라이언트이다.

Lettuce와 비슷하게 Netty를 사용하여 non-blocking I/O를 사용하며 Redisson의 특이한 점은 직접 레디스의 명령어를 제공하지 않고,   
Bucket이나 Map같은 자료구조나 Lock 같은 특정한 구현체의 형태로 제공한다.

1. Lock에 타임아웃이 구현되어있다.
```java
/**
 * waitTime  : 락 획득을 대기할 타임아웃
 * leaseTime : 락이 만료되는 시간
 * TimeUnit  : 단위
 */
public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException
```

2. 스핀 락을 사용하지 않는다.
    - Redisson 은 pubsub 기능을 사용하여 락이 해제될 때마다 subscribe하는 클라이언트들에게 알림을 주어 레디스에  
      요청을 보내 락의 획득가능여부를 체크하지 않아도 되도록 개선했다.

Sample
```java
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VoteService {

  private final VoteRepository voteRepository;
  private final RedissonClient redissonClient;

  public void voteWithoutLock(String voter, String candidate) {
    if (voteRepository.existsByVoter(voter)) {
      log.error("{} already voted", voter);
    } else {
      voteRepository.saveAndFlush(Vote.create(voter, candidate));
      log.info("{} vote completed", voter);
    }
  }

  public void voteWithLock(String voter, String candidate) {
    RLock lock = redissonClient.getLock("voter-" + voter);
    try {
      if (lock.tryLock(15, 10, TimeUnit.SECONDS)) {
        try {
          if (voteRepository.existsByVoter(voter)) {
            log.error("{} already voted", voter);
          } else {
            voteRepository.saveAndFlush(Vote.create(voter, candidate)); // 반드시 saveAndFlush 해야함
            log.info("{} vote completed", voter);
          }
        } finally {
          if (lock.isLocked())
            lock.unlock();
        }
      }
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
```

Test
```java
@SpringBootTest
@Slf4j
class VoteServiceTest {

  private static final int THREAD_SIZE = 10;

  CountDownLatch countDownLatch = new CountDownLatch(THREAD_SIZE);
  ExecutorService service = Executors.newFixedThreadPool(THREAD_SIZE);

  @Autowired
  VoteService voteService;

  @AfterEach
  void tearDown() {
    voteService.deleteAll();
  }

  @DisplayName("redisson 분산락 사용 안함")
  @Test
  void voteWithoutLock() throws Exception {
    // given
    String voter = "voter1";
    String candidate = "candidate1";

    // when
    for (int i = 0; i < THREAD_SIZE; i++) {
      service.execute(() -> {
        voteService.voteWithoutLock(voter, candidate);
        countDownLatch.countDown();
      });
    }

    countDownLatch.await();

    // then
    List<Vote> votes2 = voteService.findAll();
    votes2.forEach(vote -> log.info("vote : {}", vote));

    assertThat(votes2.size()).isGreaterThan(1);
  }

  @DisplayName("redisson 분산락 사용 함")
  @Test
  void voteWithLock() throws Exception {
    // given
    String voter = "voter2";
    String candidate = "candidate2";

    // when
    for (int i = 0; i < THREAD_SIZE; i++) {
      service.execute(() -> {
        voteService.voteWithLock(voter, candidate);
        countDownLatch.countDown();
      });
    }

    countDownLatch.await();

    // then
    List<Vote> votes = voteService.findAll();
    votes.forEach(vote -> log.info("vote : {}", vote));

    assertThat(votes.size()).isEqualTo(1);
  }
}
```

RAtomicLong 활용 동시접근시에 한번만 실행
```java
@SpringBootTest
@Slf4j
class LockUtilsTest {

  private static final int THREAD_SIZE = 10;

  CountDownLatch countDownLatch = new CountDownLatch(THREAD_SIZE);
  ExecutorService service = Executors.newFixedThreadPool(THREAD_SIZE);

  @Autowired
  RedissonClient redissonClient;

  @DisplayName("redisson RAtomicLong 을 사용하여 동시 접근시에 한번만 실행")
  @RepeatedTest(5)
  void redissonRAtomicLong() throws Exception {
    // given
    String lockKey = "lock-2";
    AtomicInteger count = new AtomicInteger(0);

    // when
    for (int i = 0; i < THREAD_SIZE; i++) {
      service.execute(() -> {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(lockKey);

        if (atomicLong.compareAndSet(0, 1)) {
          atomicLong.expire(2, TimeUnit.SECONDS);
          count.getAndIncrement();
          log.info("executed!!!");
        } else {
          log.info("already executed");
        }

        countDownLatch.countDown();
      });
    }
    countDownLatch.await();

    // then
    log.info("end");
    Assertions.assertThat(count.get()).isEqualTo(1);
    TimeUnit.SECONDS.sleep(3);
  }
}
```

***
## 주의사항   
JPA 사용시 트랜잭션이 끝나서 영속성 컨텍스트를 FLUSH 하기전에 Lock 을 해제하고 다음 작업을 하면   
동시성 이슈가 그대로 발생될수있음.

`@Transactional` 은 메소드가 종료되었을 때 메소드에 예외를 던져 트랜잭션이 롤백되는지 커밋되는지를 결정한다.  
그러나 메서드가 끝나기 전에 lock.unlock()로 잠금을 해제해야하기 때문에 동시성 문제가 있다.

해결 방법
> 방법 1. Service Layer 메서드에서 트랜잭션을 직접 열고 unlock 전에 커밋 해준다.  
> 방법 2. Layer 를 분리하여 메서드를 호출한다. ex) 컨트롤러에서 Lock 처리를 하고 Service 에서 트랜잭션 및 DB 처리를 한다.

Spring 은 ThreadLocal 변수를 통해 트랜잭션 상태를 관리하기 때문에 다른 스레드에서 시작된 트랜잭션 (ex : @Async, Executors) 은  
상위 스레드에 대해 관리되는 트랜잭션에 참여할 수 없으므로 롤백이 안된다.

***
## 참조
- [Redisson GitHub](https://github.com/redisson/redisson)
- [Redisson GitHub wiki](https://github.com/redisson/redisson/wiki/8.-distributed-locks-and-synchronizers)
- [레디스를 활용한 분산 락과 안전하고 빠른 락의 구현](https://hyperconnect.github.io/2019/11/15/redis-distributed-lock-1.html)
- [MySQL을 이용한 분산락으로 여러 서버에 걸친 동시성 관리](https://techblog.woowahan.com/2631/)
