package com.ask.springcore.locale;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LocaleController {

  private final MessageSource messageSource;

  @GetMapping(LocaleFilter.ACCEPT_LANGUAGES_PATH + "/locale")
  public String locale(@PathVariable Locale locale, @RequestParam(defaultValue = "name") String key) {
    log.info("locale : {}", locale);
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }
}
