package com.ask.hibernatejasypt;

import com.ask.hibernatejasypt.entity.User;
import com.ask.hibernatejasypt.repository.UserRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializeRunner implements ApplicationRunner {

  private final UserRepository userRepository;

  @Override
  public void run(ApplicationArguments args) {
    userRepository.saveAll(
        Arrays.asList(
            User.create("ask1", "1234"),
            User.create("ask2", "1234"),
            User.create("ask3", "data..."),
            User.create("ask4", "5dc29ee9-b9b9-4c7f-acc6-4d2dad16eba8")
        )
    );
  }

}
