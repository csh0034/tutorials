package com.ask.domainevents.domain.event;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DomainEvent {

  private final String id;
  private final LocalDateTime createdDt;

  public DomainEvent(String id) {
    this.id = id;
    this.createdDt = LocalDateTime.now();
  }

}
