package com.ask.springjparedisson;

import com.ask.springjparedisson.entity.Company;
import com.ask.springjparedisson.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

  public static String STORED_NOTICE_ID = "";

  private final CompanyRepository companyRepository;

  @Override
  public void run(ApplicationArguments args) {
    Company company = Company.create("company-name");
    companyRepository.save(company);
    STORED_NOTICE_ID = company.getId();
  }
}
