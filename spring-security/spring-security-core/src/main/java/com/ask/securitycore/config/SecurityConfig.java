package com.ask.securitycore.config;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfig {

  @Bean
  @Order(-1)
  public SecurityFilterChain ignoredPatternFilterChain() {
    return new DefaultSecurityFilterChain(new OrRequestMatcher(
        requestMatchers("/version.txt", "/service_check")
    ));
  }

  private List<RequestMatcher> requestMatchers(String... patterns) {
    return Arrays.stream(patterns)
        .map(AntPathRequestMatcher::new)
        .collect(toList());
  }

  @Bean
  @Order(0)
  public SecurityFilterChain resources(HttpSecurity http) throws Exception {
    return http.requestMatchers(matchers -> matchers
            .antMatchers("/main/css/**", "/sub/css/**"))
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().permitAll())
        .csrf().disable()
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
