package com.ask.javacore;

import com.ask.javacore.common.BaseTest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ForkJoinPoolTest extends BaseTest {

  @BeforeAll
  static void beforeAll() {
    print("availableProcessors : " + Runtime.getRuntime().availableProcessors());
  }

  @Order(1)
  @Test
  void init() {
    print("init");
  }

  @Order(2)
  @Test
  void commonPool() {
    // commonPool, default 1 less than #cores
    print("parallelism : " + ForkJoinPool.getCommonPoolParallelism());

    IntStream.rangeClosed(1, ForkJoinPool.getCommonPoolParallelism()).parallel()
        .forEach(index -> print(index, "Done"));
  }

  @Order(3)
  @Test
  void newForkJoinPool() throws Exception {
    ForkJoinPool forkJoinPool = new ForkJoinPool(11);
    print("parallelism : " + forkJoinPool.getParallelism());

    forkJoinPool.submit(() ->
        IntStream.rangeClosed(1, forkJoinPool.getParallelism()).parallel()
            .forEach(index -> print(index, "Done")));

    TimeUnit.MILLISECONDS.sleep(500);
  }

  @Order(4)
  @Test
  void commonPoolSleep()throws Exception {
    int parallelism = ForkJoinPool.getCommonPoolParallelism();

    CompletableFuture.runAsync(() ->
        IntStream.rangeClosed(1, parallelism).parallel()
          .forEach(index -> {
            try {
              TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            print(index, "Done1");
          }));

    TimeUnit.MILLISECONDS.sleep(100);

    CompletableFuture.runAsync(() ->
        IntStream.rangeClosed(1, parallelism).parallel()
            .forEach(index -> print(index, "Done2")));

    TimeUnit.MILLISECONDS.sleep(700);
  }

  @Order(5)
  @Test
  void newForkJoinPoolSleep() throws Exception {
    int parallelism = 11;

    ForkJoinPool forkJoinPool1 = new ForkJoinPool(parallelism);
    forkJoinPool1.submit(() ->
        IntStream.rangeClosed(1, parallelism).parallel()
            .forEach(index -> {
              try {
                TimeUnit.MILLISECONDS.sleep(100);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              print(index, "Done1");
            }));

    ForkJoinPool forkJoinPool2 = new ForkJoinPool(parallelism);
    forkJoinPool2.submit(() ->
        IntStream.rangeClosed(1, parallelism).parallel()
            .forEach(index -> {
              print(index, "Done2");
            }));

    TimeUnit.MILLISECONDS.sleep(500);
  }
}
