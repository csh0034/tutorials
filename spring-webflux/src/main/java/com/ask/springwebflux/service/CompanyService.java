package com.ask.springwebflux.service;

import com.ask.springwebflux.config.cache.CacheConstants;
import com.ask.springwebflux.config.cache.ReactorCache;
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

  @ReactorCache(CacheConstants.COMPANIES)
  public Flux<Company> findAllByNameContains(String name) {
    return Flux.fromStream(() -> {
          log.debug("invoke Supplier in fromStream");
          return companyRepository.findAllByNameContains(name).stream();
        })
        .subscribeOn(Schedulers.boundedElastic())
        .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("company not exists!!"))))
        .doOnError(e -> log.debug("occur error"));
  }

  public Mono<String> saveTemporaryCompany() {
    return Mono.fromCallable(() -> {
          Company company = Company.create("tempCompany", String.valueOf(System.currentTimeMillis()));
          return companyRepository.save(company);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(Company::getId);
  }

  @ReactorCache(CacheConstants.COMPANY)
  public Mono<Company> findById(String id) {
    return Mono.fromCallable(() -> companyRepository.findById(id).orElse(null))
        .subscribeOn(Schedulers.boundedElastic())
        .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("company not exists!!, id : " + id))));
  }

}
