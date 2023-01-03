package com.ask.springvalidator.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "create")
@Getter
@ToString
public class FieldError {

  private String field;
  private String value;
  private String reason;

}
