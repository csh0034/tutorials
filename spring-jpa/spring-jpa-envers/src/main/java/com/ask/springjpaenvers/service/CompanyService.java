package com.ask.springjpaenvers.service;

import com.ask.springjpaenvers.entity.Company;
import com.ask.springjpaenvers.repository.CompanyRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;

  @Transactional
  public String save(Company company) {
    return companyRepository.save(company).getId();
  }

  public Company find(String companyId) {
    return companyRepository.findById(companyId).orElseThrow(EntityNotFoundException::new);
  }

  public Revisions<Long, Company> findRevisions(String companyId) {
    return companyRepository.findRevisions(companyId);
  }

  public Optional<Revision<Long, Company>> findLastChangeRevision(String companyId) {
    return companyRepository.findLastChangeRevision(companyId);
  }
}
