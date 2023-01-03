package com.ask.springvalidator.advice;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler
  public BindingResult methodArgumentNotValidException(BindException e) {
    return e.getBindingResult();
  }

}
