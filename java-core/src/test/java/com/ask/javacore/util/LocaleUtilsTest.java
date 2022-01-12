package com.ask.javacore.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.ask.javacore.common.BaseTest;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class LocaleUtilsTest extends BaseTest {

  @Test
  void availableLocales() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    locales.forEach(locale -> print(locale.toString()));
  }

  @Test
  void toLocale() {
    // given
    String locale = "ko_KR";

    // when
    Locale result = LocaleUtils.toLocale(locale);

    // then
    assertThat(result).isEqualTo(new Locale("ko", "KR"));
  }

  @Test
  void toLocaleException() {
    // given
    String locale = "invalid";

    // when then
    assertThatIllegalArgumentException().isThrownBy(() -> LocaleUtils.toLocale(locale));
  }

  @Test
  void languagesByCountry() {
    // given
    String countryCode = "KR";

    // when
    List<Locale> locales = LocaleUtils.languagesByCountry(countryCode);

    // then
    locales.forEach(locale -> print(locale.toString()));

    assertThat(locales).hasSize(1);
    assertThat(locales).contains(new Locale("ko", "KR"));
  }

  @Test
  void countriesByLanguage() {
    // given
    String languageCode = "ko";

    // when
    List<Locale> locales = LocaleUtils.countriesByLanguage(languageCode);

    // then
    locales.forEach(locale -> print(locale.toString()));

    assertThat(locales).hasSize(1);
    assertThat(locales).contains(new Locale("ko", "KR"));
  }
}
