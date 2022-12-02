package com.ask.springredis.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;

/**
 * @see DataRedisTest
 * DataRedisTest 를 사용하면 키 만료처리 등의 기능이 동작하지 않는다.
 */
@DataRedisTest
@Slf4j
class AccountRepositoryTest {

  @Autowired
  private AccountRepository accountRepository;

  @Test
  void save() {
    Account account = Account.create("name...", 10);
    accountRepository.save(account);
    log.info("account: {}", account);

    Optional<Account> result = accountRepository.findById(account.getId());
    assertThat(result).isPresent();
  }

}
