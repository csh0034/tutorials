package com.ask.springwebflux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@Slf4j
class FluxTest {

  @Test
  void doOnNext() {
    Flux.just(10, 20)
        .doOnNext(i -> log.info("doOnNext : {}", i))
        .subscribe(i -> log.info("subscribe : {}", i));
  }

  @Test
  void just() {
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
  public void filter() {
    Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
        .filter(np -> !np.contains(" "));

    StepVerifier.create(nationalParkFlux)
        .expectNext("Yellowstone", "Yosemite", "Zion")
        .verifyComplete();
  }

  @Test
  public void distinct() {
    Flux<String> animalFlux = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater")
        .distinct();

    StepVerifier.create(animalFlux)
        .expectNext("dog", "cat", "bird", "anteater")
        .verifyComplete();
  }

  @Test
  public void map() {
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
  public void flatMap() {
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
  public void buffer() {
    Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

    Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

    StepVerifier.create(bufferedFlux)
        .expectNext(Arrays.asList("apple", "orange", "banana"))
        .expectNext(Arrays.asList("kiwi", "strawberry"))
        .verifyComplete();
  }

  @Test
  public void bufferAndFlatMap() {
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


}
