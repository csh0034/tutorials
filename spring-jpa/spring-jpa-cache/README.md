# Spring jpa cache (second-level cache)
개발환경
- IntelliJ IDEA 2021.2
- spring boot 2.5.5
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
Cache Java Config
```java
@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

  private final CacheEventLoggerListener cacheEventLoggerListener;

  @Bean
  public JCacheManagerCustomizer jCacheManagerCustomizer() {
    return cacheManager -> {
      cacheManager.createCache("com.ask.springjpacache.entity.User", getConfiguration(new Duration(SECONDS, 5)));
      cacheManager.createCache("com.ask.springjpacache.entity.Company", getConfiguration(new Duration(SECONDS, 5)));
    };
  }

  private MutableConfiguration<String, Object> getConfiguration(Duration duration) {
    MutableConfiguration<String, Object> configuration = new MutableConfiguration<>();
    configuration.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(duration));

    MutableCacheEntryListenerConfiguration<String, Object> listenerConfiguration =
        new MutableCacheEntryListenerConfiguration<>(FactoryBuilder.factoryOf(cacheEventLoggerListener),
            null,
            true,
            true
        );
    configuration.addCacheEntryListenerConfiguration(listenerConfiguration);

    return configuration;
  }
}
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
#        javax.cache:
#          provider: org.ehcache.jsr107.EhcacheCachingProvider # 별도의 프로바이더 사용시
#          uri: classpath:ehcache.xml # 별도의 ehcache 설정
        cache:
          default_cache_concurrency_strategy: read-write
          use_query_cache: true
          use_second_level_cache: true
          region.factory_class: jcache

    open-in-view: false

logging:
  level:
    "[org.hibernate.SQL]": debug
    "[org.hibernate.type.descriptor.sql.BasicBinder]": trace
```

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

- NONE
- READ_ONLY
- NONSTRICT_READ_WRITE
- READ_WRITE
- TRANSACTIONAL


## Hibernate property
- org.hibernate.cfg.AvailableSettings - 전체 프로퍼티
- org.hibernate.cache.jcache.ConfigSettings - 캐싱 관련 프로퍼티


## 2차캐시 관련 답변내용
- 엔티티는 스프링이나 외부캐시에 저장하면 안된다.
- 엔티티는 영속성 컨텍스트에서 상태를 관리하기 때문에  DTO로 변환하여 저장해야한다.
- 하이버네이트 2차 캐시보다 스프링이 지원하는 서비스 계층 캐시를 사용하는것이 효과적임

## 참조
- [Spring Boot, second-level caching](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-access.configure-hibernate-second-level-caching)
- [Hibernate, caching](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#caching)
- [인프런, 김영한님 답변](https://www.inflearn.com/questions/33629)
- [권남님 블로그, Hibernate Cache](https://kwonnam.pe.kr/wiki/java/hibernate/cache)