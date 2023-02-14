# Spring jpa jcache (ehcache3, second-level cache)
개발환경
- spring boot 2.6.4
- Java 8
- Maven

pom.xml
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-jcache</artifactId>
</dependency>
<dependency>
  <groupId>org.ehcache</groupId>
  <artifactId>ehcache</artifactId>
</dependency>
```

JPA Config
```java
@Configuration
@RequiredArgsConstructor
public class JpaConfig {

  private final CacheManager cacheManager;

  @Bean
  public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer() {
    return (properties) -> properties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
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
      javax.persistence.sharedCache.mode: ENABLE_SELECTIVE # @Cacheable 사용시 캐싱 적용
      hibernate:
        show_sql: false
        format_sql: true
#        generate_statistics: true # Hibernate 통계정보를 출력
          javax.cache:
            missing_cache_strategy: fail
#          provider: org.ehcache.jsr107.EhcacheCachingProvider # 별도의 프로바이더 사용시
#          uri: classpath:ehcache.xml # 별도의 ehcache 설정
        cache:
          default_cache_concurrency_strategy: read-write
          use_query_cache: false # 쿼리캐시 설정 default false
          use_second_level_cache: true
          region.factory_class: jcache

    open-in-view: false

logging:
  level:
    "[org.hibernate.SQL]": debug
    "[org.hibernate.type.descriptor.sql.BasicBinder]": trace
```

### 프로퍼티 설명
`hibernate.javax.cache.missing_cache_strategy`
- 캐시설정을 하지 않을 경우 처리
> 이 방법으로 생성된 캐시는 Cache Provider 에서 기본캐시 구성을 명시적으로 하지 않을 경우  
> 크기와 TTL 설정이 없으므로 운영시엔 fail 로 해두는게 나아보임

| 값 | 설명 |
|----|-----|
|create-warn|default, 캐시 세팅이 안되어있을 경우 새 캐시를 만들면서 누락된 캐시 경고 출력|
|create|경고를 기록하지 않고 새 캐시를 만듬|
|fail|캐시 누락에 대한 예외와 함께 실패|

<br>

`hibernate.cache.use_query_cache`
- true or false
- 쿼리 캐시 사용여부 이며 고정 매개변수 값으로 자주 실행되는 쿼리에 유용함
> 쿼리 결과를 캐싱하면 애플리케이션의 일반적인 트랜잭션 처리 측면에서 약간의 오버헤드가 발생한다.  
> 엔티티 변경시 쿼리 캐시가 무효화 되어야 하기 때문에 대부분의 애플리케이션이 단순히 쿼리 결과를 캐싱하는 것의 이점을 얻지 못한다.  
> 따라서 기본적으로 `false` 로 설정 되어있음

<br>

`spring.jpa.properties.hibernate.generate_statistics`
- 하이버네이트가 여러 통계정보를 출력하게 해주는데 캐시 적용 여부를 확인 가능
- executing 1 JDBC statements : DB 쿼리 수행
- L2C puts: 2차 캐시에 데이터 추가
- L2C hits: 2차 캐시의 데이터 사용
- L2C miss: 2차 캐시에 해당 데이터 조회 실패 -> 데이터베이스 접근
```text
// DB 접근시
298266 nanoseconds spent acquiring 1 JDBC connections;
0 nanoseconds spent releasing 0 JDBC connections;
244403 nanoseconds spent preparing 1 JDBC statements;
844024 nanoseconds spent executing 1 JDBC statements;
0 nanoseconds spent executing 0 JDBC batches;
2532453 nanoseconds spent performing 1 L2C puts;
0 nanoseconds spent performing 0 L2C hits;
1726149 nanoseconds spent performing 1 L2C misses;
0 nanoseconds spent executing 0 flushes (flushing a total of 0 entities and 0 collections);
0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)

// 2차캐시 접근시
26572 nanoseconds spent acquiring 1 JDBC connections;
0 nanoseconds spent releasing 0 JDBC connections;
0 nanoseconds spent preparing 0 JDBC statements;
0 nanoseconds spent executing 0 JDBC statements;
0 nanoseconds spent executing 0 JDBC batches;
0 nanoseconds spent performing 0 L2C puts;
1367875 nanoseconds spent performing 1 L2C hits;
0 nanoseconds spent performing 0 L2C misses;
0 nanoseconds spent executing 0 flushes (flushing a total of 0 entities and 0 collections);
0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)
```

### RegionFactoryInitiator
hibernate.cache.region.factory_class 프로퍼티가 설정이 안되어 있을 경우 클래스패스에서  
RegionFactory 를 찾는다.

### MetadataBuildingOptionsImpl
MetadataBuildingOptionsImpl 의 inner class MetadataBuildingOptionsImpl  
생성자에서 엔티티에 대하여 프로퍼티 설정함

### Shared Cache Mode
javax.persistence.SharedCacheMode  
- ALL : 모든 엔티티에 적용
- NONE : 사용하지 않음
- ENABLE_SELECTIVE : 명시적으로 javax.persistence.Cacheable, @Cacheable 사용 해야함
- DISABLE_SELECTIVE : @Cacheable(false) 인 엔티티만 제외
- UNSPECIFIED : 정의되지 않음, 벤더에 따라 달라짐, Hibernate default

### Concurrency Strategy
글로벌 설정 : `hibernate.cache.default_cache_concurrency_strategy=read-write`  
엔티티 설정 : `@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)`
> Hibernate 문서에선 전역 설정을 사용하는 것보다 엔티티별로 캐시 동시성 전략을 정의하는 것을 추천함.

- NONE
  - 동시성 전략을 적용하지 않는다.
- READ_ONLY
  - 데이터를 추가 및 제거할 수 있지만 변경할 수는 없습니다. (엔터티를 업데이트 시도시 예외 발생).
  - 변경되지 않는 일부 정적 참조 데이터에 매우 적합
  - Entity에 @Immutable annotation 선언 되어야함
- NONSTRICT_READ_WRITE
  - 두 개의 개별 트랜잭션 스레드가 동일한 개체를 업데이트할 가능성이 낮은 경우 사용
  - 캐시를 잠그지 않고 업데이트되는 데이터를 캐싱한다, 일관성이 보장되지 않으며
  - 최종 일관성을 허용할 수 있는 사용 사례에 적합하다
- READ_WRITE
  - READ_COMMITTED 정도의 격리 수준을 보장
  - 데이터를 업데이트 해야 하는 경우에 적합
  - READ_WRITE + 클러스터링 캐시 구현체를 사용할 경우 캐시 구현체는 Lock 기능을 제공해야만 한다
  - 캐시된 엔터티가 업데이트되면 해당 엔터티에 대한 소프트 잠금도 캐시에 저장되며 트랜잭션이 커밋된 후 해제된다.  
    소프트 잠금 항목에 액세스하는 모든 동시 트랜잭션은 데이터베이스에서 해당 데이터를 직접 가져온다.
- TRANSACTIONAL
  - 설정에 따라 REPEATABLE_READ 정도의 격리 수준을 보장   
  - JTA를 사용, 따라서 엔티티가 자주 수정되는 경우에 더 적합
  - hibernate.transaction.manager_lookup_class 를 지정해야함 EHCache 지원 안함

> READ_ONLY 설정시에 Entity에 @Immutable annotation 없을 경우 하단 warning 로그 발생  
> @Immutable : 엔티티 생성 가능, 엔티티 수정 무시, 컬렉션 엔티티 수정시 HibernateException 발생
```text
HHH90001003: Read-only caching was requested for mutable entity [NavigableRole[com.ask.springjpajcache.entity.Role]]
```

## Hibernate property
- org.hibernate.cfg.AvailableSettings - 전체 프로퍼티
- org.hibernate.cache.jcache.ConfigSettings - 캐싱 관련 프로퍼티

## hibernate-ehcache
- Hibernate version < 5.3 일 경우 사용하며 현재 Deprecated 된 Ehcache2 를 사용한다
- Hibernate version >= 5.3 일 경우 hibernate-jcache 와 Ehcache3 를 사용해야 한다.

## 2차 캐시
애플리케이션에서 공유하는 캐시를 JPA는 공유 캐시(Shared Cache)라 하는데 일반적으로 2차 캐시 (Second Level Cache, L2 Cache)라 부른다.   
2차 캐시는 애플리케이션 범위의 캐시다. 따라서 애플리케이션을 종료할 때까지 캐시가 유지된다. 분산 캐시나 클러스터링 환경의 캐시는 애플리케이션보다   
더 오래 유지 될 수도 있다. 엔티티 매니저를 통해 데이터를 조회할 때 우선 2차 캐시에서 찾고 없으면 데이터베이스에서 찾는다.
- 엔티티 캐시 : 엔티티 단위로 캐시한다. 식별자로 엔티티를 조회하거나 컬렉션이 아닌 연관된 엔티티를 로딩할 때 사용한다.
- 컬렉션 캐시 : 엔티티와 연관된 컬렉션을 캐시한다. 컬렉션이 엔티티를 담고 있으면 식별자 값만 캐시한다. (하이버네이트 기능)
- 쿼리 캐시 : 쿼리와 파라미터 정보를 키로 사용해서 캐시한다. 결과가 엔티티면 식별자 값만 캐시한다. (하이버네이트 기능)

## 2차 캐시 영역
- 엔티티 캐시 영역 : 기본값으로 “패키지 명 + 클래스 명”을 사용
- 컬렉션 캐시 영역 : 엔티티 캐시 영역 이름에 캐시한 컬렉션의 필드 명이 추가
- 필요하다면 @Cache(region = "customRegion", …) 처럼 region 속성을 사용해서 캐시 영역을 직접 지정할 수 있음
- 캐시 영역별로 세부 설정이 가능


## 2차캐시 관련 답변내용
- 엔티티는 스프링이나 외부캐시에 저장하면 안된다.
- 엔티티는 영속성 컨텍스트에서 상태를 관리하기 때문에  DTO로 변환하여 저장해야한다.
- 하이버네이트 2차 캐시보다 스프링이 지원하는 서비스 계층 캐시를 사용하는것이 효과적임

## 참조
- [Spring Boot, second-level caching](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-access.configure-hibernate-second-level-caching)
- [Hibernate GitHub, caching](https://github.com/hibernate/hibernate-orm/blob/main/documentation/src/main/asciidoc/userguide/chapters/caching/Caching.adoc)
- [Hibernate, caching](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#caching)
- [redisson hibernate](https://pavankjadda.medium.com/implement-hibernate-2nd-level-cache-with-redis-spring-boot-and-spring-data-jpa-7cdbf5632883)
- [인프런, 김영한님 답변](https://www.inflearn.com/questions/33629)
- [권남님 블로그, Hibernate Cache](https://kwonnam.pe.kr/wiki/java/hibernate/cache)
- [JPA 캐시 정리 블로그](https://gunju-ko.github.io/jpa/2019/01/14/JPA-2%EC%B0%A8%EC%BA%90%EC%8B%9C.html)
- [Hazlecast, jcache](https://docs.hazelcast.com/tutorials/hibernate-jcache)
