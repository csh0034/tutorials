package com.ask.springvalidator.vo;

import com.ask.springvalidator.validator.Between;
import com.ask.springvalidator.validator.NoSpecialCharacter;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ValidatorVO1 {

  @NotBlank(message = "{name.NotBlank}")
  @NoSpecialCharacter
  private String name;

  @Between(min = 10, max = 30, message = "{age.Between}")
  private Integer age;
}
