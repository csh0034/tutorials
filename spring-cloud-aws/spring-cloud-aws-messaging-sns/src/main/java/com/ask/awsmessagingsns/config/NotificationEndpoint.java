package com.ask.awsmessagingsns.config;

import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.config.annotation.NotificationSubject;
import io.awspring.cloud.messaging.endpoint.NotificationStatus;
import io.awspring.cloud.messaging.endpoint.annotation.NotificationMessageMapping;
import io.awspring.cloud.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import io.awspring.cloud.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(SnsConstants.DEFAULT_TOPIC)
@Slf4j
public class NotificationEndpoint {

  @NotificationSubscriptionMapping
  public void handleSubscriptionMessage(NotificationStatus status) {
    log.info("handleSubscriptionMessage...");
    status.confirmSubscription();
  }

  @NotificationMessageMapping
  public void handleNotificationMessage(@NotificationSubject String subject, @NotificationMessage String message) {
    log.info("handleNotificationMessage... subject: {}, message: {}", subject, message);
  }

  @NotificationUnsubscribeConfirmationMapping
  public void handleUnsubscribeMessage() {
    log.info("handleUnsubscribeMessage...");
  }

}
