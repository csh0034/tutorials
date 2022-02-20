package com.ask.openfeign.config.client;

import java.time.format.DateTimeFormatter;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

@Configuration
public class DefaultFeignClientConfig {

  private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Bean
  public FeignFormatterRegistrar defaultDateTimeFormatterRegistrar() {
    return registry -> {
      DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
//      registrar.setUseIsoFormat(true); IsoFormat 으로 사용해야하면 이세팅만 해주면됨
      registrar.setDateFormatter(dateFormatter);
      registrar.setTimeFormatter(timeFormatter);
      registrar.setDateTimeFormatter(dateTimeFormatter);
      registrar.registerFormatters(registry);
    };
  }

}
