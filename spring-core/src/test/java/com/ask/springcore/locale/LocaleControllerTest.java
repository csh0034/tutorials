package com.ask.springcore.locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class LocaleControllerTest {

  @Autowired
  private MockMvc mvc;

  @ParameterizedTest
  @CsvSource({
      "ko, 사과",
      "en, apple",
      "es, manzana",
      "fr, pomme"
  })
  void i18n(String language, String expected) throws Exception {
    // when
    ResultActions result = mvc.perform(get("/{language}/locale", language));

    // then
    result.andExpect(content().string(expected));
  }
}
