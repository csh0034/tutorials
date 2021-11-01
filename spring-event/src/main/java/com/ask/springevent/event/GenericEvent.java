package com.ask.springevent.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class GenericEvent<T> {

  private final T entity;
  private final boolean needToPersist;
}
