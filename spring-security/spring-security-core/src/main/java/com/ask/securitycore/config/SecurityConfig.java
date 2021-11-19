package com.ask.securitycore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

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
              .loginProcessingUrl("/main/login").and())
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
              .loginProcessingUrl("/sub/login").and())
          .logout(logout -> logout
              .logoutUrl("/sub/logout")
              .logoutSuccessUrl("/"))
          .authorizeRequests(auth -> auth
              .anyRequest().authenticated());
    }
  }
}
