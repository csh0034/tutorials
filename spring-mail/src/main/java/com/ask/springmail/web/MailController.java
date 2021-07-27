package com.ask.springmail.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

  private final JavaMailSender mailSender;
  private final MailProperties mailProperties;

  @GetMapping
  public String mail() {
    mailSender.send(getSimpleMessage("to@naver.com", "test2", "body-text2"));
    return "success";
  }

  private SimpleMailMessage getSimpleMessage(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(String.format("SENDER<%s>", mailProperties.getUsername()));
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    return message;
  }
}
