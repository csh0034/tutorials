package com.ask.springvalidator.advice;

import static java.util.stream.Collectors.toMap;

import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
    return e.getBindingResult().getFieldErrors().stream()
        .collect(toMap(FieldError::getField, fieldError -> messageSource.getMessage(fieldError, Locale.getDefault())));
  }
}
