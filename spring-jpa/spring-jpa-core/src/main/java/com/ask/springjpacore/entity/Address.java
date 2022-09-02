package com.ask.springjpacore.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Address {

  private String city;
  private String street;
  private String zipcode;

}
