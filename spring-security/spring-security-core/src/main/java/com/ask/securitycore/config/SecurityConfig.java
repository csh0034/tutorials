package com.ask.securitycore.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityConfig {

  @Configuration
  @Order(1)
  public static class MainSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
      web.ignoring().antMatchers("/main/css/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/main/**")
          .csrf(AbstractHttpConfigurer::disable)
          .sessionManagement(session -> session
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .formLogin(form -> form
              .loginPage("/main/login").permitAll()
              .loginProcessingUrl("/main/login"))
          .logout(logout -> logout
              .logoutUrl("/main/logout")
              .logoutSuccessUrl("/"))
          .authorizeRequests(auth -> auth
              .anyRequest().authenticated());
    }
  }

  @Configuration
  @Order(2)
  public static class SubSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
      web.ignoring().antMatchers("/sub/css/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/sub/**")
          .csrf(AbstractHttpConfigurer::disable)
          .formLogin(form -> form
              .loginPage("/sub/login").permitAll()
              .loginProcessingUrl("/sub/login"))
          .logout(logout -> logout
              .logoutUrl("/sub/logout")
              .logoutSuccessUrl("/"))
          .sessionManagement(session -> session
              .maximumSessions(1)
              .maxSessionsPreventsLogin(true))
          .authorizeRequests(auth -> auth
              .anyRequest().authenticated());
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
      return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
  }
}
