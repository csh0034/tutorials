package com.ask.springjpalock.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpalock.entity.Company;
import com.ask.springjpalock.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Slf4j
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @DisplayName("User 생성")
  @Test
  void create() {
    // given
    Company company = Company.create("회사 이름");
    companyRepository.save(company);

    User user = User.create("ask", "1234", company);

    // when
    User savedUser = userRepository.save(user);

    // then
    log.info("user : {}", user);
    assertThat(savedUser.getId()).isNotEmpty();
    assertThat(savedUser).isEqualTo(user);
  }
}