# Spring Webflux

## reactor

### Publisher

#### Mono

0 - 1개의 데이터를 다룰때 사용

#### Flux

0 - N개의 데이터를 다룰때 사용

### Publisher 구분

- Cold Publisher : subscribe(구독) 하지않으면 데이터를 방출하지 않는다.
- Hot Publisher : subscribe(구독) 하지않아도 데이터를 방출한다.

### Publisher 생성하기

- create (Cold Publisher)
  - lower level 의 메소드로 직접적으로 데이터의 방출 및 에러신호 내보낼수있다.
- Just (Hot Publisher)
  - 처음 방출된 값을 cache 해놓고 다음 구독자에게 cache 된 값을 방출
- defer (Cold Publisher)
  - 데이터의 방출을 구독전까지 지연시킨다.
- fromCallable, fromSupplier (Cold Publisher)
  - 데이터의 방출을 구독전까지 지연시키면서 Mono 로 반환한다.
  - null 을 반환 할 경우 Mono.empty() 로 처리된다. 

### Context

명령형 프로그래밍에서 리액티브 프로그래밍으로 사고방식을 전환할때 만나는 기술적인 어려움 중 하나가 스레드 처리 방식이다.    
리액티브 프로그래밍에선 실행중 쉽게 스레드간 전환이 이루어진다.

따라서 ThreadLocal 같이 안정적인 스레드 모델을 생각하면 특히 어렵게 느껴질수있다.

reactor 3.1.0 부터 ThreadLocal 과 비슷하지만, Thread 대신 Flux, Mono 에 적용가능한 Context 를 제공한다.  
`Context` 는 Reactive Sequence 상에서 공유되는 데이터이다.


### [Synchronous, Blocking Call 처리](https://projectreactor.io/docs/core/release/reference/#faq.wrap-blocking)

```java
Mono.fromCallable(() -> {
  return /* make a remote synchronous call */
})
.subscribeOn(Schedulers.boundedElastic())
```

`Schedulers.boundedElastic()` 은 다른 논블로킹 처리에 영향을 주지 않으면서 블로킹 리소스를 기다릴 전용 스레드를 생성해주며,   
생성할 수 있는 스레드 수에 제한을 두기 때문에 요청이 몰렸을 때 블로킹 태스크를 큐에 넣어 연기시킬 수 있다.

## Troubleshooting

### Security 적용하면 Request 실행하는 Thread 가 변경됨.

Security Filter Chain 을 적용하면 초기엔 reactor-http-nio-숫자 쓰레드로 실행되지만  
`ServerRequestCacheWebFilter` 를 지나가면 다음 필터(`LogoutWebFilter`) 에선 parallel-숫자 쓰레드로 실행된다. 

### Webflux 에서 JPA AuditorAware 적용시 이슈

Spring Servlet stack 과 다르게 Webflux 사용시 인증정보를 ThreadLocal 이 아닌 Context 에 저장한다.  
이 정보를 AuditorAware 에서 꺼내올때 `AuditorAware<String> ` interface 구현후 `Optional<T>` 을 리턴해야하는데 블록킹콜을 호출하면
`blockOptional() is blocking, which is not supported in thread {thread name}` 익셉션이 발생한다.

```java
@Bean
public AuditorAware<String> auditorProvider() {
  return () -> ReactiveSecurityContextHolder.getContext()
      .map(SecurityContext::getAuthentication)
      .filter(Authentication::isAuthenticated)
      .map(Authentication::getName)
      .switchIfEmpty(Mono.just("SYSTEM"))
      .blockOptional();
}
```

Reactor 의 Context 는 Reactive Sequence 상에서 공유되는 데이터이다.   
따라서 쓰레드가 변경되어도(publishOn, subscribeOn) Context 안에 값을 사용할 수 있다. 

위의 경우에는 JPA 가 PrePost 시점에 AuditorAware 를 호출하는데 동일한 Reactive Sequence 에서 실행되는게 아니므로  
인증유저의 데이터를 가져올 수 없다.

> Jpa Audit 는 @CreatedDate 만 사용하고, @CreatedBy 는 사용 못하는것 같다..

### Webflux 에서 Cache 사용하기

Webflux 사용시엔 @Cacheable 을 사용할 수 없다.  
따라서 reactor-extra 라이브러리를 추가하여 CacheMono, CacheFlux 를 사용해야한다.

## 참조

- [Project Reactor](https://projectreactor.io/docs/core/release/reference/)
- [Project Reactor, 번역](https://godekdls.github.io/Reactor%20Core/contents/)
- [Blog 1](https://tries1.github.io/spring/2020/01/28/spring_webflux_1.html)
- [Blog 2, reactor cache](https://dreamchaser3.tistory.com/m/17)
