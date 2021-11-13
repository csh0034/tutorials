package com.ask.multitenancy.repository.tenant;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.multitenancy.entity.tenant.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompanyRepositoryTest {

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  void save() {
    // given
    Company company = Company.create("sample-" + System.currentTimeMillis());

    // when
    companyRepository.saveAndFlush(company);

    // then
    assertThat(company.getId()).isNotNull();
  }
}