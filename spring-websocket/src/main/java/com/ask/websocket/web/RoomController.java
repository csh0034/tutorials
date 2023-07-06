package com.ask.websocket.web;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RoomController {

  private final SimpMessagingTemplate messagingTemplate;

  @GetMapping("/room")
  public String room() {
    return "room";
  }

  @MessageMapping("/room")
  @SendTo("/topic/room")
  public Object handle(Map<String, Object> message) {
    log.info("message: {}", message);
    return message;
  }

  @MessageMapping("/queue/reply")
  @SendToUser("/queue/message")
  public Object reply(Map<String, Object> message) {
    log.info("message: {}", message);
    return message;
  }

  @SubscribeMapping("/subscribe")
  public Object subscribe() {
    log.info("message: {}", "구독시에 전송되는 메세지");
    return "hi";
  }

  @GetMapping("/direct")
  @ResponseBody
  public String direct(String username, String message) {
    messagingTemplate.convertAndSendToUser(username, "/queue/message" ,message);
    return "ok";
  }

  @Getter
  @Setter
  public static class ChatMessage {
    private String writer;
    private String message;
  }

}
