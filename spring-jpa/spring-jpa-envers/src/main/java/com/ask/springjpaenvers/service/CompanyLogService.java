package com.ask.springjpaenvers.service;

import com.ask.springjpaenvers.entity.CompanyLog;
import com.ask.springjpaenvers.repository.CompanyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyLogService {

  private final CompanyLogRepository companyLogRepository;

  @Transactional
  public void save(CompanyLog companyLog) {
    companyLogRepository.save(companyLog);
  }
}
