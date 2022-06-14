package com.ask.awssecretsmanager;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.awssecretsmanager.config.CustomProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringCloudAwsSecretsManagerApplicationTests {

  @Autowired
  private CustomProperties customProperties;

  @Test
  void validate() {
    assertThat(customProperties).usingRecursiveComparison().isEqualTo(new CustomProperties("ASk", "1234"));
  }


}
