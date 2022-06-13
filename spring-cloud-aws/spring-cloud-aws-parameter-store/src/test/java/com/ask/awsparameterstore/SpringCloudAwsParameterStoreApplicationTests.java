package com.ask.awsparameterstore;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.awsparameterstore.config.CustomProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringCloudAwsParameterStoreApplicationTests {

  @Autowired
  private CustomProperties customProperties;

  @Test
  void validate() {
    assertThat(customProperties).usingRecursiveComparison().isEqualTo(new CustomProperties("ASk", "1234"));
  }

}
