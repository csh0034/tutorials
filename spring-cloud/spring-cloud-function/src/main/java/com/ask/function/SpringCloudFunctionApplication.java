package com.ask.function;

import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringCloudFunctionApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudFunctionApplication.class, args);
  }

  @Bean
  public Function<Mono<String>, Mono<String>> uppercase() {
    return mono -> mono.map(String::toUpperCase);
  }

  @Bean
  public Supplier<Mono<String>> timestamp() {
    return () -> Mono.just(String.valueOf(System.currentTimeMillis()));
  }
}
