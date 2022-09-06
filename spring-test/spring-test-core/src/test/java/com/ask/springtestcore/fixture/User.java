package com.ask.springtestcore.fixture;

import static lombok.AccessLevel.PROTECTED;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = PROTECTED)
@ToString
public class User {

  private String name;
  private int age;
  private String team;
  private String countryCode;

  @Builder
  private User(String name, int age, String countryCode) {
    this.name = name;
    this.age = age;
    this.countryCode = countryCode;
  }

  public void assignTeam(String team) {
    this.team = team;
  }

}
