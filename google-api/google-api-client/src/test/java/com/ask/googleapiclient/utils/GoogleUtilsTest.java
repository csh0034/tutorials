package com.ask.googleapiclient.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.googleapiclient.utils.GoogleUtils.GoogleProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GoogleUtilsTest {

  @Disabled
  @Test
  void googleProfile() {
    GoogleProfile googleProfile = GoogleUtils.googleProfile("clientId", "accessToken");
    assertThat(googleProfile).isNotNull();
  }

}
