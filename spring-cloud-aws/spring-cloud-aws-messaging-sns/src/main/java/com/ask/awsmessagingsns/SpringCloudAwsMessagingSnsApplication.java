package com.ask.awsmessagingsns;

import com.ask.awsmessagingsns.config.SnsConstants;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringCloudAwsMessagingSnsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudAwsMessagingSnsApplication.class, args);
  }

  @Bean
  public ApplicationRunner applicationRunner(NotificationMessagingTemplate messagingTemplate) {
    return args -> {
      log.info("send message after 3 seconds");

      CompletableFuture.runAsync(() -> {
        try {
          TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        log.info("sendNotification...");
        messagingTemplate.sendNotification(SnsConstants.DEFAULT_TOPIC, "메세지!!", "제목!!");
      });
    };
  }

}
