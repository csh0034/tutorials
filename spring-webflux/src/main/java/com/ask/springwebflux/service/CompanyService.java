package com.ask.springwebflux.service;

import com.ask.springwebflux.entity.Company;
import com.ask.springwebflux.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;

  public Flux<Company> findAll() {

    return Flux.fromStream(() -> {
          log.info("invoke Supplier in fromStream");
          return companyRepository.findAll().stream();
        })
        .subscribeOn(Schedulers.boundedElastic())
        .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("company not exists!!"))))
        .doOnError(e -> log.info("occur error"))
        .log();
  }

}
