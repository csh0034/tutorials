package com.ask.stream;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringCloudStreamApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudStreamApplication.class, args);
  }

  @Bean
  public Function<String, String> uppercase() {
    return value -> {
      log.info("Received(uppercase): {}", value);
      return value.toUpperCase();
    };
  }

  @Bean
  public Consumer<String> print() {
    return s -> log.info("Received(print): {}", s);
  }

  @Bean
  public ApplicationRunner applicationRunner(StreamBridge streamBridge) {
    return args -> {
      streamBridge.send("uppercase-in-0", "uppercase-in-0 message");
      streamBridge.send("uppercase-out-0", "uppercase-out-0 message");
      streamBridge.send("print-in-0", "print-in-0 message");
    };
  }
}
