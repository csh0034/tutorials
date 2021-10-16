package com.ask.springvalidator.vo;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ValidatorVO3 {

  @AssertTrue(message = "field true")
  private Boolean exists1;

  @AssertFalse(message = "field false")
  private Boolean exists2;

  @AssertTrue(message = "all field must not be null")
  public boolean isAllFieldNotNull() {
    return exists1 != null && exists2 != null;
  }
}
