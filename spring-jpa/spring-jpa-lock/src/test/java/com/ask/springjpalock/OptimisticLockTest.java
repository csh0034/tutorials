package com.ask.springjpalock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ask.springjpalock.entity.Company;
import com.ask.springjpalock.service.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootTest
class OptimisticLockTest {

  @Autowired
  private CompanyService companyService;

  @DisplayName("낙관적 잠금 Exception 발생")
  @Test
  void test1() {
    // given
    String companyId = companyService.saveCompany(Company.create("companyName"));
    Company company = companyService.findById(companyId);

    // then
    company.updateName("update1");
    companyService.saveCompany(company);

    company.updateName("update2");
    assertThrows(ObjectOptimisticLockingFailureException.class, () -> companyService.saveCompany(company));
  }

  @DisplayName("@Lock(LockModeType.OPTIMISTIC), 조회시에도 버전 체크")
  @Test
  void test2() {
    // given
    companyService.saveCompany(Company.create("company1"));

    // when
    Company company = companyService.findCompanyByName("company1");

    // then
    assertNotNull(company);
  }

}
