package com.ask.springcore.locale;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class LocaleFilter extends OncePerRequestFilter {

  public static final List<String> ACCEPT_LANGUAGES = Arrays.asList("ko", "en", "fr", "es");
  public static final String ACCEPT_LANGUAGES_PATH = "/{locale:ko|en|fr|es}";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String path = request.getRequestURI();

    ACCEPT_LANGUAGES.stream()
        .filter(language -> path.startsWith("/" + language))
        .findFirst()
        .ifPresent(language -> LocaleContextHolder.setLocale(new Locale(language)));

    filterChain.doFilter(request, response);
  }
}
