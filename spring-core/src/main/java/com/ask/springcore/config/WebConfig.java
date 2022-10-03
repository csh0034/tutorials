package com.ask.springcore.config;

import com.ask.springcore.config.converter.LenientStringToEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverterFactory(new LenientStringToEnumConverterFactory());
//    ApplicationConversionService.configure(registry);
  }

}
