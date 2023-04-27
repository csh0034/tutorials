package com.ask.securitycore.matcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

class IpAddressMatcherTest {

  @Test
  void match() {
    IpAddressMatcher matcher = new IpAddressMatcher("10.0.0.0/24");

    assertThat(matcher.matches("10.0.0.0")).isTrue();
    assertThat(matcher.matches("10.0.0.255")).isTrue();

    assertThat(matcher.matches("10.0.1.0")).isFalse();
    assertThat(matcher.matches("10.0.1.255")).isFalse();

    assertThatIllegalArgumentException().isThrownBy(() -> matcher.matches("10.0.0.256"));
  }

}
