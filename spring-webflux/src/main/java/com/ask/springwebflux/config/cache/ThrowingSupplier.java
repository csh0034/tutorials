package com.ask.springwebflux.config.cache;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {

  @Override
  default T get() {
    try {
      return getThrows();
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  T getThrows() throws Throwable;

}
