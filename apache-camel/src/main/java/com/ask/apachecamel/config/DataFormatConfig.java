package com.ask.apachecamel.config;

import lombok.RequiredArgsConstructor;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.apache.camel.dataformat.beanio.springboot.BeanIODataFormatConfiguration;
import org.apache.camel.spi.DataFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataFormatConfig {

  private final BeanIODataFormatConfiguration beanIODataFormatConfiguration;

  @Bean
  public DataFormat dataFormat() {
    return new BeanIODataFormat(beanIODataFormatConfiguration.getMapping(), beanIODataFormatConfiguration.getStreamName());
  }

}
