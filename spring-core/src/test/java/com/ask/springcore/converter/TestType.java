package com.ask.springcore.converter;

import com.ask.springcore.config.converter.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TestType implements Code {
  A_TYPE("1"),
  B_TYPE("2");

  private final String code;
}
