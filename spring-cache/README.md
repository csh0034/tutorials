# spring boot cache

## [Supported Cache Providers](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.caching.provider)
1. Generic : Cache 타입의 빈을 여러개 생성 했을 경우
2. JCache (JSR-107) (EhCache 3, Hazelcast, Infinispan, and others)
3. EhCache 2.x
4. Hazelcast
5. Infinispan
6. Couchbase
7. Redis
8. Caffeine
9. Simple : 위의 Provider 가 없을 경우 등록됨
   - ConcurrentMapCacheManager 로 구성
   - cacheNames 를 지정할 경우, 지정된 캐시만 등록가능
   - cacheNames 를 지정하지 않을 경우 다이나믹 하게 등록됨
10. None : 특정 환경에서 캐시를 강제로 사용하지 않게함