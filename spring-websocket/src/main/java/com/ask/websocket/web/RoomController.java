package com.ask.websocket.web;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class RoomController {

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

  @Getter
  @Setter
  public static class ChatMessage {
    private String writer;
    private String message;
  }

}
