# Spring Data Redis

## Redis 란?

- Remote Dictionary Server
- 고성능 키-값 저장소로서 비정형 데이터 저장하며 다양한 데이터 구조를 지원하는 NoSQL 이다.
- Redis is an open source, in-memory data structure store
  - database
  - cache
  - message broker
  - streaming engine.

### [Redis Data Type](https://redis.io/docs/data-types/)

- Strings
- Sets
- Sorted Sets
- Hashes
- Lists
- ...

### [Redis Persistence](https://redis.io/docs/management/persistence/)

- RDB(Redis Database)
- AOF(Append Only File)
- No persistence
- RDB + AOF

## Redis keyspace notifications

- 새로운 키 입력/변경 등의 이벤트가 발생할 때 알려주는 기능(Pub/Sub)
- 예를 들어 하단과 같은 이벤트를 받을수 있다.
  - ex. 1) 주어진 key 에 대해 영향을 끼치는 모든 command
  - ex. 2) database 0 에서 만료된 모든 키들
- Redis Pub/Sub 의 경우 `fire and forget` 이다. 클라이언트 연결이 끊어졌다가  
  다시 연결될 경우 그 사이의 발행된 모든 이벤트(메세지) 는 유실된다.
- 따라서 신뢰할 수 있는 알림(reliable notification)에 대한 요구가 있다면 적합하지 않다.
  - MQTT 의 경우 QoS 가 있어 더 적합할것 같다.

### 설정

- 기본적으로 keyspace event notification 기능은 약간의 cpu 성능이 필요하기 때문에 비활성화되어 있다.
- redis.conf 의 notify-keyspace-events 옵션을 사용하거나 CONFIG SET 을 통해서 활성화 할 수 있다.

```text
K     Keyspace events, published with __keyspace@<db>__ prefix.
E     Keyevent events, published with __keyevent@<db>__ prefix.
g     Generic commands (non-type specific) like DEL, EXPIRE, RENAME, ...
$     String commands
l     List commands
s     Set commands
h     Hash commands
z     Sorted set commands
t     Stream commands
x     Expired events (events generated every time a key expires)
e     Evicted events (events generated when a key is evicted for maxmemory)
A     Alias for g$lshztxe, so that the "AKE" string means all the events.
```

### 모든 이벤트 활성화

```shell
config set notify-keyspace-events KEA
```

### pub/sub 테스트

```shell
$ config set notify-keyspace-events KEA

# console A
$ redis-cli --csv psubscribe '__keyevent@*__:*'
Reading messages... (press Ctrl-C to quit)
"psubscribe","__keyevent@*__:*",1

# console B
$ redis-cli SET key1 value1 ex 3

# console A
"pmessage","__keyevent@*__:*","__keyevent@0__:set","key1"
"pmessage","__keyevent@*__:*","__keyevent@0__:expire","key1"
"pmessage","__keyevent@*__:*","__keyevent@0__:expired","key1"
```

### spring data redis 사용시

```java
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
```

### 

내부에서 하단과 같이 호출

```shell
config set notify-keyspace-events Ex
```

`__keyevent@*__:expired` 토픽을 subscribe 한다.

### spring redis session 사용시

- ConfigureNotifyKeyspaceEventsAction

```properties
spring.session.redis.configure-action=notify_keyspace_events # default
```

내부에서 하단과 같이 호출

```shell
config set notify-keyspace-events Egx
```

### AWS Elasticache 사용시

기본적으로 기능이 비활성화 되어있으며 [config 명령어도 막혀있다.](https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/RestrictedCommands.html)   
따라서 콘솔에서 파라미터 세팅을 해주어야 한다.

- [AWS ElastiCache 키스페이스 알림을 구현](https://aws.amazon.com/ko/premiumsupport/knowledge-center/elasticache-redis-keyspace-notifications/)
- ElastiCache 에 Config set 을 호출하지 않도록 하단과 같이 세팅해야 한다.

```java
// keyspaceNotificationsConfigParameter 빈값일 경우 호출 안함 
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP, keyspaceNotificationsConfigParameter = "")
```

```properties
# config set notify-keyspace-events 호출 안하도록 설정 
spring.session.redis.configure-action=none
```

## 참조

- [Reference, Spring Data Redis](https://docs.spring.io/spring-data/redis/docs/current/reference/html)
- [Reference, Redis keyspace notifications](https://redis.io/docs/manual/keyspace-notifications/)
- [Docker Image, Redis](https://hub.docker.com/_/redis)
