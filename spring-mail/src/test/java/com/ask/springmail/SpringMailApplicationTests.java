package com.ask.springmail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Disabled
@SpringBootTest
class SpringMailApplicationTests {
  
  @Autowired
  private JavaMailSender mailSender;

  /**
   * gmail 의 경우 subject 가 cache 등을 통해 안바뀌는줄 알았는데
   * 설정에서 대화형식으로 보기(default 활성화) 가 켜져있으면 하나로 묶이는 기능이 있어서 그랬던거였음.
   */
  @ParameterizedTest
  @ValueSource(strings = {"[예약] 초대장: 예약 타이틀", "[예약취소] 초대장: 예약 타이틀", "[수정됨] 초대장: 예약 타이틀"})
  void mailSend(String subject) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(String.format("no-reply<%s>", "csh0034@gmail.com"));
    message.setTo("shchoi1@rsupport.com", "4536508@naver.com");
    message.setSubject(subject);
    message.setText(subject);
    mailSender.send(message);
  }

}
