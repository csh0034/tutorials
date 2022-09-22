package com.ask.mapstruct.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Car {

  private String id;
  private String name;

  @Builder
  private Car(String id, String name) {
    this.id = id;
    this.name = name;
  }

}
