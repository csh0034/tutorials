package com.ask.springjpaquerydsl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.ask.springjpaquerydsl.entity.Company;
import com.ask.springjpaquerydsl.entity.User;
import com.ask.springjpaquerydsl.repository.CompanyRepository;
import com.ask.springjpaquerydsl.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
@Slf4j
class EntityTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @DisplayName("id 만 있는 detach 상태 entity 로 연관관계 대상과 persist 되는지 검증")
  @Test
  void saveWithDetachEntity() {
    // given
    Company company = Company.create("company1");
    companyRepository.save(company);

    Company detachCompany = createDetachCompany(company.getId());

    // when
    User createdUser = User.create("abc123", "1234", detachCompany);
    userRepository.save(createdUser);

    // then
    log.info("createUser : {}", createdUser);
  }

  @DisplayName("db 에 존재하지않는 id의 entity 로 연관관계 대상과 persist 되는지 검증")
  @Test
  void saveWithDetachEntityOccurException() {
    // given
    Company detachCompany = createDetachCompany("not-exists-id");

    // when then
    User createdUser = User.create("abc123", "1234", detachCompany);

    assertThatExceptionOfType(DataIntegrityViolationException.class)
        .isThrownBy(() -> userRepository.save(createdUser));
  }

  @DisplayName("id 만 있는 detach 상태 entity 저장 검증")
  @Test
  void saveDetachEntity() {
    // given
    Company company = Company.create("company1");
    companyRepository.save(company);

    // when
    Company detachCompany = createDetachCompany(company.getId());
    companyRepository.save(detachCompany);

    // then
    log.info("detachCompany : {}", detachCompany);
  }

  private Company createDetachCompany(String companyId) {
    Company company = new Company();
    company.setId(companyId);
    return company;
  }

}
