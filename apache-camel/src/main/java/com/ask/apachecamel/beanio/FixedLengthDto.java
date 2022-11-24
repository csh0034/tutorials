package com.ask.apachecamel.beanio;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FixedLengthDto {

  private Map<String, Object> header = new HashMap<>();
  private Map<String, Object> data = new HashMap<>();
  private String end;

}
