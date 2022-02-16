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

### [Synchronous, Blocking Call 처리](https://projectreactor.io/docs/core/release/reference/#faq.wrap-blocking)

```java
Mono.fromCallable(() -> {
  return /* make a remote synchronous call */
})
.subscribeOn(Schedulers.boundedElastic())
```

`Schedulers.boundedElastic()` 은 다른 논블로킹 처리에 영향을 주지 않으면서 블로킹 리소스를 기다릴 전용 스레드를 생성해주며,   
생성할 수 있는 스레드 수에 제한을 두기 때문에 요청이 몰렸을 때 블로킹 태스크를 큐에 넣어 연기시킬 수 있다.


## 참조

- [Project Reactor](https://projectreactor.io/docs/core/release/reference/)
- [Project Reactor, 번역](https://godekdls.github.io/Reactor%20Core/contents/)
- [Blog 1](https://tries1.github.io/spring/2020/01/28/spring_webflux_1.html)
