package com.ask.springvalidator.vo;

import com.ask.springvalidator.validator.Phone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ValidatorVO2 {

  @Phone
  String phone;
}
