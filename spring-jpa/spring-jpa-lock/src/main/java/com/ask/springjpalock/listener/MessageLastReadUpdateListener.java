package com.ask.springjpalock.listener;

import com.ask.springjpalock.service.RoomMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MessageLastReadUpdateListener {

  private final RoomMessageService roomMessageService;

  @TransactionalEventListener
  public void handle(MessageLastReadIdUpdateEvent event) {
    roomMessageService.updateMessageLastReadId(event.roomMemberId, event.messageLastReadId);
  }

  @RequiredArgsConstructor
  public static class MessageLastReadIdUpdateEvent {

    private final String roomMemberId;
    private final String messageLastReadId;

  }

}
