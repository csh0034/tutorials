package com.ask.springcore.converter;

import com.ask.springcore.config.converter.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Company implements Code {
  A_GOOGLE("1"),
  A_APPLE("2"),
  A_META("3");

  private final String code;

}
