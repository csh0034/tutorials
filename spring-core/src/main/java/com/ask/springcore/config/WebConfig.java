package com.ask.springcore.config;

import com.ask.springcore.config.converter.LenientStringToEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    // Converter 는 Bean 으로 등록만하면 자동으로 동작하지만 ConverterFactory 는 직접 등록해줘야함
    registry.addConverterFactory(new LenientStringToEnumConverterFactory());
//    ApplicationConversionService.configure(registry);
  }

}
