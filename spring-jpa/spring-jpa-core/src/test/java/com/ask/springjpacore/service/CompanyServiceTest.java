package com.ask.springjpacore.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpacore.entity.Company;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CompanyServiceTest {

  @Autowired
  private CompanyService companyService;

  @Test
  void create() {
    // given
    Company company = Company.create("ask-company");

    // when
    Company savedCompany = companyService.saveAndFlush(company);

    // then
    log.info("company: {}", company);
    assertThat(savedCompany.getId()).isNotEmpty();
    assertThat(savedCompany).isEqualTo(company);
  }

}
