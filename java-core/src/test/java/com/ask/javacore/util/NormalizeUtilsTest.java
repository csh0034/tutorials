package com.ask.javacore.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import org.junit.jupiter.api.Test;

class NormalizeUtilsTest {

  @Test
  void nfc() {
    String value = "컴퓨터";
    String nfd = Normalizer.normalize(value, Form.NFD);

    String nfc = NormalizeUtils.normalizeNfc(nfd);

    assertThat(nfc.length()).isEqualTo(3);
    assertThat(nfc.toCharArray()).containsExactly('컴', '퓨', '터');
  }

  @Test
  void nfd() {
    String value = "컴퓨터";

    String nfd = Normalizer.normalize(value, Form.NFD);
    System.out.println("nfd: " + nfd);
    System.out.println("nfd.length: " + nfd.length());

    for (char c : nfd.toCharArray()) {
      System.out.println(c);
    }

    assertThat(nfd.length()).isEqualTo(7);
  }

}
