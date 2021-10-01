package com.ask.springcore.inject.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SecondComponent implements ComponentInterface {

  @Override
  public void printName() {
    log.info("SecondBean");
  }
}