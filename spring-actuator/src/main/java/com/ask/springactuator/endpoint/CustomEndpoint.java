package com.ask.springactuator.endpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

@Configuration
@Endpoint(id = "custom")
//@WebEndpoint(id = "custom") // web 으로만 노출
public class CustomEndpoint {

  private final Map<String, String> map = new ConcurrentHashMap<>();

  @ReadOperation
  public Map<String, String> customs() {
    return map;
  }

  @ReadOperation
  public String custom(@Selector String custom) {
    return map.get(custom);
  }

  @WriteOperation
  public void updateCustom(String custom, String value) {
    map.put(custom, value);
  }

  @DeleteOperation
  public void clearCustoms() {
    map.clear();
  }

  @DeleteOperation
  public void clearCustom(@Selector String custom) {
    map.remove(custom);
  }
}
