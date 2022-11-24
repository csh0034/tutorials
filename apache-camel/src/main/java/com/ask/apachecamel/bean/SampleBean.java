package com.ask.apachecamel.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleBean {

  public Object invoke(Object input) {
    log.info("[SampleBean.invoke] input: {}", input);
    return input;
  }

}
