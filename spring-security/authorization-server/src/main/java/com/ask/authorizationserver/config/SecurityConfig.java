package com.ask.authorizationserver.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .headers().frameOptions().sameOrigin().and()
        .csrf().disable()
        .authorizeRequests(authorizeRequests ->
            authorizeRequests.anyRequest().permitAll()
        )
        .formLogin(withDefaults());
    return http.build();
  }

  @Bean
  UserDetailsService users() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("user1")
        .password("password")
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(user);
  }
}
