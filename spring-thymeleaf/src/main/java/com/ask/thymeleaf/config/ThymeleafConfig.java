package com.ask.thymeleaf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 메일 처리를 위해 사용하므로 view resolver 등록이 필요하지 않아
 * ThymeleafAutoConfiguration 를 exclude 하고 SpringTemplateEngine 을 별도 Bean 으로 추가
 */
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
    springResourceTemplateResolver.setPrefix("classpath:templates/mail");
    springResourceTemplateResolver.setSuffix(".html");
    springResourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
    springResourceTemplateResolver.setCharacterEncoding("UTF-8");
    springResourceTemplateResolver.setCacheable(false);
    return springResourceTemplateResolver;
  }

}
