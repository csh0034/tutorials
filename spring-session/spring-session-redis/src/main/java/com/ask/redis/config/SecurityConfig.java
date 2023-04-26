package com.ask.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .sessionManagement(session -> session.maximumSessions(1)
            .expiredUrl("/login?error=expired"))
        .authorizeHttpRequests(authorize -> authorize
            .antMatchers("/login").permitAll()
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/login") //.permitAll() 을 할 경우 loginPage, loginProcessingUrl, failureUrl 에 대해 permitAll 이 등록된다.
            .defaultSuccessUrl("/"))
        .build();
  }

  /**
   * @see ConcurrentSessionControlAuthenticationStrategy
   * @see ConcurrentSessionFilter
   */
  @Bean
  public SessionRegistry sessionRegistry(RedisIndexedSessionRepository sessionRepository) {
    return new SpringSessionBackedSessionRegistry<>(sessionRepository);
  }

}
