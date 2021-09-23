package com.ask.integration.producer.web;

import static com.ask.integration.producer.config.AmqpConfig.QUEUE_NAME_1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProduceController {

  private final RabbitTemplate rabbitTemplate;

  @GetMapping("/produce")
  public Map<String, String> produce(){

    Map<String, String> map = new HashMap<>();
    map.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    map.put("ProduceController", "produce");

    // 특정 EXCHANGE_NAME, ROUTING_KEY 사용
    //rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, map);

    // default exchange 사용, 라우팅키는 큐이름
    rabbitTemplate.convertAndSend(QUEUE_NAME_1, map);

    return map;
  }
}
