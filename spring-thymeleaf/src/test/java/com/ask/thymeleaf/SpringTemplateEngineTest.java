package com.ask.thymeleaf;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@Slf4j
class SpringTemplateEngineTest {

  @Autowired
  private TemplateEngine templateEngine;

  @Test
  void render() {
    // given
    Map<String, Object> variables = new HashMap<>();
    variables.put("title", "10 글자보다 긴 이름");
    variables.put("id", "d0993b3f-d0f9-4971-8e16-1547fe55e285");
    variables.put("email", "test@test.com");
    variables.put("name", "sample name");

    // when
    String htmlContent = templateEngine.process("/sample", new Context(Locale.getDefault(), variables));

    // then
    log.info("htmlContent: \n{}", htmlContent);
    assertThat(htmlContent).contains("<p>안녕하세요!, sample name</p>");
  }

}
