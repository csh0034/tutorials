package com.ask.springvalidator.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ValidatorVO1 {

  @NotBlank
  private String name;

  @Min(10)
  @Max(30)
  private Integer age;
}
