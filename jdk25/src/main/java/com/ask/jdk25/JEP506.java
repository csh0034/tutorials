// JEP 506: Scoped Values
// 메서드 매개변수를 사용하지 않고 메서드에 안전하고 효율적으로 데이터를 공유
package com.ask.jdk25;

import java.util.concurrent.Executors;

public class JEP506 {
  public static final ScopedValue<String> USER = ScopedValue.newInstance();

  static void main() {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      executor.submit(() -> ScopedValue.where(USER, "Alice").run(() -> {
        System.out.println("Thread: " + Thread.currentThread());
        System.out.println("User: " + USER.get());
      }));

      executor.submit(() -> ScopedValue.where(USER, "Bob").run(() -> {
        System.out.println("Thread: " + Thread.currentThread());
        System.out.println("User: " + USER.get());
      }));

      // Optional delay to ensure output appears before main exits
      Thread.sleep(200);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
