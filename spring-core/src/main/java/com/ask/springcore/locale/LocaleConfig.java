package com.ask.springcore.locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;

@Configuration
public class LocaleConfig {

  @Bean
  public LocaleFilter localeFilter() {
    return new LocaleFilter();
  }

  @Bean(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
  public AbstractLocaleContextResolver localeResolver() {
    return new AbstractLocaleContextResolver() {

      @Override
      public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
        if (localeContext == null) {
          localeContext = new SimpleLocaleContext(request.getLocale());
        }
        return localeContext;
      }

      @Override
      public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        throw new UnsupportedOperationException("Not supported");
      }
    };
  }
}
