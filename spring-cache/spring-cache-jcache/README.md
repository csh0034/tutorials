# spring cache(Jcache-Ehcache)

## JSR 과 JSR-107
- JSR : (Java Specification Requests) 사양 및 기술적 변경에 대한 정식 제안 문서. 개인 및 조직은 JCP (Java Community Process)의 회원이  
   될 수 있으며 JSR에 언급 된 스펙에 따라 코드를 개발할 수 있다. 개발 된 기술적 변화는 JCP 회원들의 검토를 거쳐 승인된다.

- JSR-107 : (JCACHE – Java Temporary Caching API) 객체 생성, 공유 액세스, 스풀링, 무효화 및 JVM 전반에 걸친 일관성을 포함하여 Java 객체의  
  메모리 캐싱에서 사용할 API 에 대한 기준으로 볼 수 있다. EhCache, Hazelcast, Redisson,Infinispan 등등 여러 구현체가 있다.

## Ehcache3 - JCache 설정

JCache API 를 통한 설정
```java
@Bean
  public JCacheManagerCustomizer jCacheManagerCustomizer() {
  return cacheManager -> {
    MutableConfiguration<Long, String> configuration = new MutableConfiguration<Long, String>()
    .setTypes(Long.class, String.class)
    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
  };
}
```

Ehcache configuration 을 통한 설정
```java
@Bean
public JCacheManagerCustomizer jCacheManagerCustomizer() {
  return cacheManager -> {
    cacheManager.createCache("current-millis", getConfiguration(2, Duration.ofSeconds(3)));
    cacheManager.createCache("sample-name", getConfiguration(10, Duration.ofSeconds(10)));
  };
}

private javax.cache.configuration.Configuration<String, Object> getConfiguration(long heap, Duration ttl) {
  return Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder.newCacheConfigurationBuilder(
          String.class, Object.class, ResourcePoolsBuilder.heap(heap))
      .withSizeOfMaxObjectSize(1000, MemoryUnit.B)
      .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
      .build());
}
```

xml 을 통한 설정
```yaml
spring:
  cache:
    type: jcache #구현체 하나면 생략가능
    jcache:
      config: classpath:ehcache.xml
```

## TTL, TTI
TTL 과 TTI 동시 설정 불가
- TTL : 지정한 시간 만큼 유지
- TTI : 지정한 시간 만큼 유지하며 access 할 경우 추가로 TTI 만큼 늘어남

## 참조
- [ehcache3](https://www.ehcache.org/documentation/3.0/xml.html)
- [spring boot, JCache(JSR-107)](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.caching.provider.jcache)
- [spring, JCache(JSR-107)](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache-jsr-107)