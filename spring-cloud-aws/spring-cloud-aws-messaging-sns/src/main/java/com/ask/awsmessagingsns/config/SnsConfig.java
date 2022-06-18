package com.ask.awsmessagingsns.config;

import com.amazonaws.services.sns.AmazonSNS;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnsConfig {

  @Bean
  public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSns) {
    return new NotificationMessagingTemplate(amazonSns);
  }

}
