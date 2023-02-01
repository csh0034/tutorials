package com.ask.mongodb;

import com.ask.mongodb.domain.Item;
import com.ask.mongodb.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringMongodbApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringMongodbApplication.class, args);
  }

  @Bean
  public ApplicationRunner applicationRunner(ItemRepository itemRepository) {
    return args -> {
      itemRepository.save(new Item("name11", 5, "snacks"));
      itemRepository.save(new Item("name22", 2, "drinks"));

      log.info("{}", itemRepository.findById("63d9fbad10e06a6798d2699f").orElse(null));
    };
  }

}
