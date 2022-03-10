package com.ask.springmail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class SpringMailApplicationTests {
  
  @Autowired
  private JavaMailSender mailSender;

  @Test
  void mailSend() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(String.format("SENDER<%s>", "no-reply@remotemeeting.com"));
    message.setTo("shchoi1@rsupport.com");
    message.setSubject("subject...");
    message.setText("body...");

    mailSender.send(message);
  }

}
