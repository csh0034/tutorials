package com.ask.sheets;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest
@Slf4j
class GoogleSheetsApplicationTest {

  @Autowired
  private MessageSource messageSource;

  @Test
  void languageResource() {
    List<String> codes = Arrays.asList("ROLE_ADMIN", "ROLE_USER", "ROLE_SYSTEM");
    List<Locale> locales = Arrays.asList(Locale.KOREAN, Locale.ENGLISH, Locale.JAPANESE);

    for (String code : codes) {
      for (Locale locale : locales) {
        log.info("{}, {}", code, messageSource.getMessage(code, null, locale));
      }
    }
  }

}
