package com.ask.springjpaenvers.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ask.springjpaenvers.entity.Company;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

@SpringBootTest
@AutoConfigureTestDatabase
@Slf4j
class CompanyServiceTest {

  @Autowired
  private CompanyService companyService;

  @DisplayName("Company 저장 후 Revisions 조회")
  @Test
  void test1() {
    // given
    Company company = Company.create("companyName1");

    // when
    String companyId = companyService.save(company);
    for (int i = 0; i < 20; i++) {
      Company savedCompany = companyService.find(companyId);
      savedCompany.updateName("i:" + i);
      companyService.save(savedCompany);
    }

    // then
    Revisions<Long, Company> revisions = companyService.findRevisions(companyId);
    revisions.forEach(revision -> log.info("revision : {}", revision));
  }

  @DisplayName("Company 저장 후 최근Revision 조회")
  @Test
  void test2() {
    // given
    Company company = Company.create("companyName2");

    // when
    String companyId = companyService.save(company);

    // then
    Optional<Revision<Long, Company>> lastChangeRevision = companyService.findLastChangeRevision(companyId);
    assertTrue(lastChangeRevision.isPresent());

    log.info("revision : {}", lastChangeRevision.get());
  }
}