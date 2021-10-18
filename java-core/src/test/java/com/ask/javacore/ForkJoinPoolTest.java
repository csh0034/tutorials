package com.ask.javacore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForkJoinPoolTest {

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
        .forEach(index -> print(index, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
  }

  @Order(3)
  @Test
  void newForkJoinPool() throws Exception {
    ForkJoinPool forkJoinPool = new ForkJoinPool(11);
    print("parallelism : " + forkJoinPool.getParallelism());

    forkJoinPool.submit(() ->
        IntStream.rangeClosed(1, forkJoinPool.getParallelism()).parallel()
            .forEach(index -> print(index, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))));

    TimeUnit.MILLISECONDS.sleep(500);
  }

  @Order(4)
  @Test
  void commonPoolSleep() {
    int parallelism = ForkJoinPool.getCommonPoolParallelism();

    IntStream.rangeClosed(1, parallelism).parallel()
        .forEach(index ->  {
          try {
            TimeUnit.SECONDS.sleep(2);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          print(index, "Done");
        });

    print("================================================");

    IntStream.rangeClosed(1, parallelism).parallel()
        .forEach(index -> print(index, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
  }

  @Order(5)
  @Test
  void newForkJoinPoolSleep() throws Exception {
    int parallelism = 11;
    ForkJoinPool forkJoinPool1 = new ForkJoinPool(parallelism);

    forkJoinPool1.submit(() ->
        IntStream.rangeClosed(1, parallelism).parallel()
          .forEach(index ->  {
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
            .forEach(index ->  {
              print(index, "Done2");
            }));

    TimeUnit.MILLISECONDS.sleep(500);
  }

  private static void print(String message) {
    System.out.printf("(%s) %s%n", Thread.currentThread().getName(), message);
  }

  private static void print(int index, String message) {
    System.out.printf("(%s) index=%d %s%n", Thread.currentThread().getName(), index, message);
  }
}
