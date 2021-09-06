package com.ask.springjpalock.service;

import com.ask.springjpalock.entity.Company;
import com.ask.springjpalock.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;

  public Company findById(String companyId) {
    return companyRepository.findById(companyId).orElseThrow(IllegalStateException::new);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public String saveCompany(Company company) {
    log.info("before version : {}", company.getVersion());
    Company savedCompany = companyRepository.saveAndFlush(company);
    log.info("after version : {}", savedCompany.getVersion());
    return savedCompany.getId();
  }

  public Company findCompanyByName(String name) {
    return companyRepository.findOptimisticLockCompanyByName(name);
  }

  public void decreaseCount(String companyId) {
    Company company = findById(companyId);
    company.decreaseCount();
  }

  public void decreaseCountWithPessimisticLock(String companyId) {
    Company company = companyRepository.findPessimisticLockCompanyById(companyId);
    company.decreaseCount();
  }
}
