package com.ask.springjpacore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpacore.entity.Company;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
@Slf4j
class CompanyRepositoryTest {

  @Autowired
  private CompanyRepository companyRepository;

  @BeforeEach
  void setUp() {
    companyRepository.save(Company.create("Company.."));
  }

  @Nested
  class JpaSpecificationExecutorTest {

    @Test
    void spec() {
      // given
      Specification<Company> specification =  (root, query, builder) -> builder.like(root.get("name"), "Company%");

      // when
      List<Company> companies = companyRepository.findAll(specification);

      // then
      assertThat(companies).hasSize(1);
    }

  }

}
