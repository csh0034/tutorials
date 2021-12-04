# Spring jpa redisson (redis, second-level cache)

## Settings
개발환경
- IntelliJ IDEA 2021.3
- spring boot 2.6.1
- Java 8
- Maven

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
  <artifactId>redisson-hibernate-53</artifactId>
  <version>3.16.2</version>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```
Java Config
```java
@Configuration
public class JpaConfig {

  @Bean
  public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer() {
    return (properties) -> {
      // cache definition applied to all caches in entity region
      properties.put("hibernate.cache.redisson.entity.eviction.max_entries", "1000");
      properties.put("hibernate.cache.redisson.entity.expiration.time_to_live", "10000");
      properties.put("hibernate.cache.redisson.entity.expiration.max_idle_time", "7000");

      // cache definition applied to all caches in collection region
      properties.put("hibernate.cache.redisson.collection.eviction.max_entries", "1000");
      properties.put("hibernate.cache.redisson.collection.expiration.time_to_live", "10000");
      properties.put("hibernate.cache.redisson.collection.expiration.max_idle_time", "7000");

      // cache definition for entity region. Example region name: "sample_region"
      properties.put("hibernate.cache.redisson.sample_region.eviction.max_entries", "1000");
      properties.put("hibernate.cache.redisson.sample_region.expiration.time_to_live", "1000");
//      properties.put("hibernate.cache.redisson.entity.expiration.max_idle_time", "7000");
    };
  }
}
```
application.yml
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE
    username: sa

  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      javax.persistence.sharedCache.mode: ENABLE_SELECTIVE
      hibernate:
        show_sql: false
        format_sql: true
        #        generate_statistics: true
        cache:
          redisson:
            config: redisson.yml
            fallback: true
          default_cache_concurrency_strategy: read-write
          use_second_level_cache: true
          region.factory_class: org.redisson.hibernate.RedissonRegionFactory

    open-in-view: true

logging:
  level:
    "[org.hibernate.SQL]": debug
    "[org.hibernate.type.descriptor.sql.BasicBinder]": trace
```

redisson.yml
```yaml
singleServerConfig:
  address: "redis://127.0.0.1:6379"
```

## Redis based Hibernate Second-level Cache
Redis 기반의 Hibernate 2nd level Cache 구현
- open source version
  - RedissonRegionFactory
- Redisson PRO
  - RedissonLocalCachedRegionFactory
  - RedissonClusteredRegionFactory
  - RedissonClusteredLocalCachedRegionFactory

### 정리
- RedissonRegionFactory 에서 RedissonClient 를 만들때 [redisson.yml](https://github.com/redisson/redisson/wiki/2.-Configuration#221-yaml-file-based-configuration) 을 지정해서만 등록 가능
- Region 별로 Redisson RMapCache 를 이용하여 저장
- RedissonStorage 에서 getFromCache 를 통하여 RMapCache 에 접근
- Redis 에는 TTL 이 설정되지 않고 내부적으로 EvictionScheduler 에 의해 삭제됨
- 엔티티별로 TTL 설정은 안됨, Region 에 설정후 `@Cache(region = "sample_region")` 사용

### 프로퍼티 설명
```xml
<!-- 2nd level cache activation -->
<property name="hibernate.cache.use_second_level_cache" value="true" />
<property name="hibernate.cache.use_query_cache" value="false" />
  
<!-- Redisson Region Cache factory -->
<property name="hibernate.cache.region.factory_class" value="org.redisson.hibernate.RedissonRegionFactory" />

<!-- Redisson can fallback on database if Redis cache is unavailable -->
<property name="hibernate.cache.redisson.fallback" value="true" />

<!-- Redisson YAML config (located in filesystem or classpath) -->
<property name="hibernate.cache.redisson.config" value="/redisson.yaml" />

<!-- cache definition applied to all caches in entity region -->
<property name="hibernate.cache.redisson.entity.eviction.max_entries" value="10000" />
<property name="hibernate.cache.redisson.entity.expiration.time_to_live" value="600000" />
<property name="hibernate.cache.redisson.entity.expiration.max_idle_time" value="300000" />

<!-- cache definition for entity region. Example region name: "my_object" -->
<property name="hibernate.cache.redisson.my_object.eviction.max_entries" value="10000" />
<property name="hibernate.cache.redisson.my_object.expiration.time_to_live" value="300000" />
<property name="hibernate.cache.redisson.my_object.expiration.max_idle_time" value="100000" />
```

### time_to_live, max_idle_time 차이 
- time_to_live : Redis의 캐시 항목당 TTL(Time to Live)입니다. 밀리초 단위로 정의
- max_idle_time : Redis의 캐시 항목당 최대 유휴 시간 정의
- 둘다 설정 되어 있을 경우
  - Math.min(ttlExpiry, ttiExpiry) < now -> DB 쿼리
  - ttlExpiry = creationTime + timeToLive
  - ttiExpiry = mostRecentAccessTime + timeToIdle
> TTL 60초, TTI 2초 로 동시에 설정 할 경우   
> 2초이내 계속해서 엑세스 하더라도(유휴상태 아님) 60초가 되면 DB 쿼리 요청  
> 2초이내 엑세스 안할 경우 DB 쿼리 요청

## 참조
- [redisson, hibernate]https://github.com/redisson/redisson/tree/master/redisson-hibernate