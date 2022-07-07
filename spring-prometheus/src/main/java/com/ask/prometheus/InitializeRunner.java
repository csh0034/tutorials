package com.ask.prometheus;

import com.ask.prometheus.entity.Endpoint;
import com.ask.prometheus.repository.EndpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializeRunner implements ApplicationRunner {

  private final EndpointRepository endpointRepository;

  @Override
  public void run(ApplicationArguments args) {
    endpointRepository.save(Endpoint.create("ASk.."));
  }

}
