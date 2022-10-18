package com.ask.multitenancy.service;

import com.ask.multitenancy.repository.tenant.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;

  @Async
  public void printAllAsync() {
    log.info("findAllAsync: {}", companyRepository.findAll());
  }

}
