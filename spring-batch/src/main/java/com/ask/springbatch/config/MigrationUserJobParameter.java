package com.ask.springbatch.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Getter
@ToString
public class MigrationUserJobParameter {

  @Value("#{jobParameters[file]}")
  private String file;
}
