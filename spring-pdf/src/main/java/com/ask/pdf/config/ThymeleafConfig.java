package com.ask.pdf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig {

  @Bean
  public TemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(springResourceTemplateResolver());
    return templateEngine;
  }

  @Bean
  public SpringResourceTemplateResolver springResourceTemplateResolver() {
    SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
    springResourceTemplateResolver.setPrefix("classpath:templates");
    springResourceTemplateResolver.setSuffix(".html");
    springResourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
    springResourceTemplateResolver.setCharacterEncoding("UTF-8");
    springResourceTemplateResolver.setCacheable(false);
    return springResourceTemplateResolver;
  }

}
