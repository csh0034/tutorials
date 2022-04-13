package com.ask.securitycore.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityConfig {

  @Bean
  @Order(0)
  public SecurityFilterChain resources(HttpSecurity http) throws Exception {
    return http.requestMatchers(matchers -> matchers
            .antMatchers("/main/css/**", "/sub/css/**"))
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().permitAll())
        .requestCache().disable()
        .securityContext().disable()
        .sessionManagement().disable()
        .build();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain mainFilterChain(HttpSecurity http) throws Exception {
    return http.antMatcher("/main/**")
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
            .anyRequest().authenticated())
        .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain subFilterChain(HttpSecurity http) throws Exception {
    return http.antMatcher("/sub/**")
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
            .anyRequest().authenticated())
        .build();
  }

  @Bean
  public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
  }

}
