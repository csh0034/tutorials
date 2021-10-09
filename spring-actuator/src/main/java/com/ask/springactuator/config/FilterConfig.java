package com.ask.springactuator.config;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FilterConfig {

  private final DispatcherServlet dispatcherServlet;

  @PostConstruct
  public void init() {
    dispatcherServlet.setThreadContextInheritable(true);
  }

  @Bean
  public OrderedFilter customLogFilter() {
    return new OrderedFilter() {
      @Override
      public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
          throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("URI : {}", request.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);
      }

      @Override
      public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
      }
    };
  }
}
