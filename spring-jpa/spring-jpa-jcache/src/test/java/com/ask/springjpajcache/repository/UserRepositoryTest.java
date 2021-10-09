package com.ask.springjpajcache.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

import com.ask.springjpajcache.config.CacheEventLoggerListener;
import com.ask.springjpajcache.entity.Company;
import com.ask.springjpajcache.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@Slf4j
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  CompanyRepository companyRepository;

  @SpyBean
  CacheEventLoggerListener cacheEventLoggerListener;

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
    then(cacheEventLoggerListener).should(atLeastOnce()).onCreated(any());

    User findUser1 = userRepository.findById(savedUser.getId()).orElse(null);
    User findUser2 = userRepository.findById(savedUser.getId()).orElse(null);
    User findUser3 = userRepository.findById(savedUser.getId()).orElse(null);
    User findUser4 = userRepository.findById(savedUser.getId()).orElse(null);

    log.info("findUser1 : {}", findUser1);
    log.info("findUser2 : {}", findUser2);
    log.info("findUser3 : {}", findUser3);
    log.info("findUser4 : {}", findUser4);
  }
}