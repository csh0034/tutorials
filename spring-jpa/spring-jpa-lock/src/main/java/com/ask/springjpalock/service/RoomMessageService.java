package com.ask.springjpalock.service;

import com.ask.springjpalock.entity.RoomMember;
import com.ask.springjpalock.entity.RoomMessage;
import com.ask.springjpalock.listener.MessageLastReadUpdateListener.MessageLastReadIdUpdateEvent;
import com.ask.springjpalock.repository.RoomMemberRepository;
import com.ask.springjpalock.repository.RoomMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomMessageService {

  private final RoomMessageRepository roomMessageRepository;
  private final RoomMemberRepository roomMemberRepository;
  private final ApplicationEventPublisher eventPublisher;

  public void save(String message, String senderId) {
    RoomMember roomMember = roomMemberRepository.findById(senderId).orElseThrow(RuntimeException::new);

    RoomMessage roomMessage = new RoomMessage();
    roomMessage.setMessage(message);
    roomMessage.setSender(roomMember);
    String roomMessageId = roomMessageRepository.save(roomMessage).getId();

    roomMember.setMessageLastReadId(roomMessageId);
  }

  public void saveWithPessimisticLock(String message, String senderId) {
    RoomMember roomMember = roomMemberRepository.findForUpdateById(senderId).orElseThrow(RuntimeException::new);

    RoomMessage roomMessage = new RoomMessage();
    roomMessage.setMessage(message);
    roomMessage.setSender(roomMember);
    String roomMessageId = roomMessageRepository.save(roomMessage).getId();

    roomMember.setMessageLastReadId(roomMessageId);
  }

  public void saveWithoutDeadlock(String message, String senderId) {
    RoomMember roomMember = roomMemberRepository.findById(senderId).orElseThrow(RuntimeException::new);
    RoomMessage roomMessage = new RoomMessage();
    roomMessage.setMessage(message);
    roomMessage.setSender(roomMember);
    String roomMessageId = roomMessageRepository.save(roomMessage).getId();

    eventPublisher.publishEvent(new MessageLastReadIdUpdateEvent(senderId, roomMessageId));
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateMessageLastReadId(String roomMemberId, String messageLastReadId) {
    RoomMember roomMember = roomMemberRepository.findById(roomMemberId).orElseThrow(RuntimeException::new);
    roomMember.setMessageLastReadId(messageLastReadId);
  }

}
