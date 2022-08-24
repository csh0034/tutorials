package com.ask.springwebflux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;
import reactor.util.function.Tuple2;

@Slf4j
class ReactorTest {

  @Test
  void doOnNext() {
    Flux.just(10, 20)
        .doOnNext(i -> log.info("doOnNext : {}", i))
        .subscribe(i -> log.info("subscribe : {}", i));
  }

  @Test
  void just() {
    //  즉시 시퀀스를 만든다.
    Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana");

    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .verifyComplete();
  }

  @Test
  void fromArray() {
    String[] fruits = {"Apple", "Orange", "Grape", "Banana"};

    Flux.fromArray(fruits)
        .as(StepVerifier::create)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .verifyComplete();
  }

  @Test
  void fromIterable() {
    List<String> fruits = Arrays.asList("Apple", "Orange", "Grape", "Banana");

    Flux.fromIterable(fruits)
        .as(StepVerifier::create)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .verifyComplete();
  }

  @Test
  void range() {
    Flux.range(1, 5)
        .log()
        .as(StepVerifier::create)
        .expectNextCount(5)
        .verifyComplete();
  }

  @Test
  void interval() {
    Flux.interval(Duration.ofSeconds(1))
        .take(3)
        .log()
        .as(StepVerifier::create)
        .expectNext(0L)
        .expectNext(1L)
        .expectNext(2L)
        .verifyComplete();
  }

  @Test
  void justWithSupplier() {
    Mono.just(((Supplier<String>) () -> {
          log.info("invoke supplier");
          return "ASk";
        }).get())
        .log()
        .as(StepVerifier::create)
        .expectNext("ASk")
        .verifyComplete();
  }

  @Test
  void fromSupplier() {
    Mono.fromSupplier(() -> {
          log.info("invoke supplier");
          return "ASk";
        })
        .log()
        .as(StepVerifier::create)
        .expectNext("ASk")
        .verifyComplete();
  }

  @Test
  void defer() {
    Mono.defer(() -> {
          log.info("invoke supplier in defer");
          return Mono.just("ASk");
        })
        .log()
        .as(StepVerifier::create)
        .expectNext("ASk")
        .verifyComplete();
  }

  @Test
  void justAndFromSupplierAndDefer() {
    Mono<Double> monoJust = Mono.just(Math.random());
    Mono<Double> monoFromSupplier = Mono.fromSupplier(Math::random);
    Mono<Double> monoDefer = Mono.defer(() -> Mono.just(Math.random()));

    // just 는 처음에 방출한 값을 내부적으로 cache 한 후 재사용
    // random 값 모두 같은결과 나옴
    System.out.println("just >>>>>>>>>>..");
    monoJust.subscribe(System.out::println);
    monoJust.subscribe(System.out::println);
    monoJust.subscribe(System.out::println);

    // fromSupplier 는 매번 새로운 값을 방출
    System.out.println("fromSupplier >>>>>>>>>>..");
    monoFromSupplier.subscribe(System.out::println);
    monoFromSupplier.subscribe(System.out::println);
    monoFromSupplier.subscribe(System.out::println);

    // defer 는 매번 새로운 값을 방출
    // defer 를 사용하여 Hot Publisher 를 Cold Publisher 로 변경하는 방법이기도 함
    System.out.println("defer >>>>>>>>>>..");
    monoDefer.subscribe(System.out::println);
    monoDefer.subscribe(System.out::println);
    monoDefer.subscribe(System.out::println);
  }

  @Test
  void merge() {
    Flux<String> lowerFlux = Flux.just("a", "b", "c")
        .delayElements(Duration.ofMillis(500));

    Flux<String> upperFlux = Flux.just("A", "B", "C")
        .delaySubscription(Duration.ofMillis(250))  // 250ms 이후에 방출
        .delayElements(Duration.ofMillis(500));     // 500ms 마다 방출

    Flux<String> mergeFlux = lowerFlux.mergeWith(upperFlux);

    StepVerifier.create(mergeFlux)
        .expectNext("a")
        .expectNext("A")
        .expectNext("b")
        .expectNext("B")
        .expectNext("c")
        .expectNext("C")
        .verifyComplete();
  }

  @Test
  void zip() {
    Flux<String> lowerFlux = Flux.just("a", "b", "c");
    Flux<String> upperFlux = Flux.just("A", "B", "C");

    Flux<Tuple2<String, String>> zippedFlux = Flux.zip(lowerFlux, upperFlux);

    StepVerifier.create(zippedFlux)
        .expectNextMatches(p -> p.getT1().equals("a") && p.getT2().equals("A"))
        .expectNextMatches(p -> p.getT1().equals("b") && p.getT2().equals("B"))
        .expectNextMatches(p -> p.getT1().equals("c") && p.getT2().equals("C"))
        .verifyComplete();
  }

  @Test
  void zipToObject() {
    Flux<String> lowerFlux = Flux.just("a", "b", "c");
    Flux<String> upperFlux = Flux.just("A", "B", "C");

    Flux<String> zippedFlux = Flux.zip(lowerFlux, upperFlux, (l, u) -> l + u);

    StepVerifier.create(zippedFlux)
        .expectNext("aA")
        .expectNext("bB")
        .expectNext("cC")
        .verifyComplete();
  }

  @Test
  void filter() {
    Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
        .filter(np -> !np.contains(" "));

    StepVerifier.create(nationalParkFlux)
        .expectNext("Yellowstone", "Yosemite", "Zion")
        .verifyComplete();
  }

  @Test
  void distinct() {
    Flux<String> animalFlux = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater")
        .distinct();

    StepVerifier.create(animalFlux)
        .expectNext("dog", "cat", "bird", "anteater")
        .verifyComplete();
  }

  @Test
  void map() {
    Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
        .map(n -> {
          String[] split = n.split("\\s");
          return new Player(split[0], split[1]);
        });

    StepVerifier.create(playerFlux)
        .expectNext(new Player("Michael", "Jordan"))
        .expectNext(new Player("Scottie", "Pippen"))
        .expectNext(new Player("Steve", "Kerr"))
        .verifyComplete();
  }

  @Test
  void flatMap() {
    Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
        .flatMap(n -> Mono.just(n)
            .map(p -> {
              String[] split = p.split("\\s");
              return new Player(split[0], split[1]);
            })
            .subscribeOn(Schedulers.parallel()) // 구독을 병렬 스레드로 수행
        );

    List<Player> playerList = Arrays.asList(
        new Player("Michael", "Jordan"),
        new Player("Scottie", "Pippen"),
        new Player("Steve", "Kerr"));

    StepVerifier.create(playerFlux)
        .expectNextMatches(playerList::contains)
        .expectNextMatches(playerList::contains)
        .expectNextMatches(playerList::contains)
        .verifyComplete();
  }

  @Test
  void buffer() {
    Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

    Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

    StepVerifier.create(bufferedFlux)
        .expectNext(Arrays.asList("apple", "orange", "banana"))
        .expectNext(Arrays.asList("kiwi", "strawberry"))
        .verifyComplete();
  }

  @Test
  void bufferAndFlatMap() {
    Flux.just("apple", "orange", "banana", "kiwi", "strawberry")
        .buffer(3)
        .flatMap(x -> Flux.fromIterable(x)
            .map(String::toUpperCase)
            .subscribeOn(Schedulers.boundedElastic())
            .log()
        )
        .subscribe();
  }

  @RequiredArgsConstructor
  @EqualsAndHashCode
  public static class Player {

    private final String first;
    private final String last;
  }

  @Test
  void flatMapSequential() {
    Flux.just("apple", "banana", "melon", "mango", "grape", "strawberry", "eggplant", "watermelon", "kiwi")
        .window(1)
        .flatMap(f -> f.map(s -> {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              return s.toUpperCase();
            })
            .subscribeOn(Schedulers.parallel()))
        .doOnNext(System.out::println)
        .blockLast();
  }

  @Test
  void collectList() {
    Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

    Mono<List<String>> fruitListMono = fruitFlux.collectList();

    StepVerifier.create(fruitListMono)
        .expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
        .verifyComplete();
  }

  @Test
  void collectMap() {
    Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

    Mono<Map<Character, String>> animalMapMono = animalFlux.collectMap(a -> a.charAt(0));

    StepVerifier.create(animalMapMono)
        .expectNextMatches(map -> map.size() == 3
            && map.get('a').equals("aardvark")
            && map.get('e').equals("eagle")
            && map.get('k').equals("kangaroo"))
        .verifyComplete();
  }

  @Test
  void all() {
    Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

    Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));
    StepVerifier.create(hasAMono)
        .expectNext(true)
        .verifyComplete();

    Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));
    StepVerifier.create(hasKMono)
        .expectNext(false)
        .verifyComplete();
  }

  @Test
  void any() {
    Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

    Mono<Boolean> hasAMono = animalFlux.any(a -> a.contains("a"));
    StepVerifier.create(hasAMono)
        .expectNext(true)
        .verifyComplete();

    Mono<Boolean> hasZMono = animalFlux.any(a -> a.contains("z"));
    StepVerifier.create(hasZMono)
        .expectNext(false)
        .verifyComplete();
  }

  @Test
  void error() {
    Mono.error(() -> new RuntimeException("runtime exception !!"))
        .log()
        .as(StepVerifier::create)
        .verifyError();
  }

  @Test
  void deferWithError() {
    Mono.defer(() -> {
          throw new RuntimeException("runtime exception !!");
        })
        .log()
        .as(StepVerifier::create)
        .verifyError();
  }

  @Test
  void fromCallableWithError() {
    Mono.fromCallable(() -> {
          throw new RuntimeException("runtime exception !!");
        })
        .log()
        .as(StepVerifier::create)
        .verifyError();
  }

  @Test
  void runBlockingCode() {
    Mono.fromCallable(() -> {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          return "ASk";
        })
        .subscribeOn(Schedulers.boundedElastic())
        .log()
        .as(StepVerifier::create)
        .expectNext("ASk")
        .verifyComplete();
  }

  @Test
  void switchIfEmpty() {
    Mono.empty()
        .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("runtime exception !!"))))
        .log()
        .as(StepVerifier::create)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void publishOn() {
    Flux.range(1, 2)
        .log()
        .doOnNext(i -> log.info("range: {}", i))
        .publishOn(Schedulers.newSingle("ooo"))
        .map(i -> 10 + i)
        .doOnNext(i -> log.info("map1: {}", i))
        .publishOn(Schedulers.newSingle("***"))
        .map(i -> "value " + i)
        .doOnNext(i -> log.info("map2: {}", i))
        .subscribe();
  }

  @Test
  void subscribeOn() {
    Flux.range(1, 2)
        .log()
        .doOnNext(i -> log.info("range: {}", i))
        .subscribeOn(Schedulers.newSingle("@@@1"))
        .map(i -> 10 + i)
        .doOnNext(i -> log.info("map1: {}", i))
        .subscribeOn(Schedulers.newSingle("@@@2"))
        .map(i -> "value " + i)
        .doOnNext(i -> log.info("map2: {}", i))
        .subscribeOn(Schedulers.newSingle("@@@3"))
        .subscribe();
  }

  @Test
  void publishOnAndSubscribeOn() {
    Flux.range(1, 2)
        .log()
        .doOnNext(i -> log.info("range: {}", i))
        .subscribeOn(Schedulers.newSingle("###1"))
        .publishOn(Schedulers.newSingle("ooo"))
        .map(i -> 10 + i)
        .doOnNext(i -> log.info("map1: {}", i))
        .subscribeOn(Schedulers.newSingle("###2"))
        .publishOn(Schedulers.newSingle("***"))
        .map(i -> "value " + i)
        .doOnNext(i -> log.info("map2: {}", i))
        .subscribeOn(Schedulers.newSingle("###3"))
        .subscribe();
  }

  @DisplayName("withInitialContext 세팅 및 검증")
  @Test
  void context1() {
    StepVerifier.create(Mono.just(1).map(i -> i + 10),
            StepVerifierOptions.create().withInitialContext(Context.of("thing1", "thing2")))
        .expectAccessibleContext()
        .contains("thing1", "thing2")
        .then()
        .expectNext(11)
        .verifyComplete();
  }

  @DisplayName("contextWrite 및 검증")
  @Test
  void context2() {
    Mono.just("ASk")
        .log()
        .contextWrite(context -> context.put("age", 29))
        .as(StepVerifier::create)
        .expectAccessibleContext()
        .hasKey("age")
        .contains("age", 29)
        .hasSize(1)
        .then()
        .expectNext("ASk")
        .verifyComplete();
  }

  @DisplayName("deferContextual 검증")
  @Test
  void context3() {
    Mono.just("ASk")
        .log()
        .flatMap(name -> Mono.deferContextual(contextView -> Mono.just(name + " : " + contextView.get("lang"))))
        .contextWrite(context -> context.put("lang", "ko"))
        .as(StepVerifier::create)
        .expectNext("ASk : ko")
        .verifyComplete();
  }

  @DisplayName("thread 가 변경되어도 Context 값 처리 가능한지 검증")
  @Test
  void context4() {
    Mono.just("ASk")
        .log()
        .subscribeOn(Schedulers.newSingle("subscribeOn"))
        .publishOn(Schedulers.newSingle("publishOn-A"))
        .flatMap(name -> {
          log.info("thread : {}", Thread.currentThread().getName());
          return Mono.deferContextual(contextView -> Mono.just(contextView.get("lang")));
        })
        .publishOn(Schedulers.newSingle("publishOn-B"))
        .flatMap(name -> {
          log.info("thread : {}", Thread.currentThread().getName());
          return Mono.deferContextual(contextView -> Mono.just(contextView.get("lang")));
        })
        .contextWrite(context -> context.put("lang", "ko"))
        .as(StepVerifier::create)
        .expectNext("ko")
        .verifyComplete();
  }

  @Test
  void flatMapMany() {
    SampleListVO sampleListVO = new SampleListVO();
    sampleListVO.names = Arrays.asList("a", "b", "c", "d", "e", "f");

    Mono.just(sampleListVO)
        .flatMap(vo -> Mono.just(vo.names))
        .flatMapMany(Flux::fromIterable)
        .as(StepVerifier::create)
        .expectNextCount(sampleListVO.names.size())
        .verifyComplete();
  }

  private static class SampleListVO {

    private List<String> names;

  }

}
