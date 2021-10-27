package com.ask.configclient.web;

import com.ask.configclient.config.ProfileProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

  private final ProfileProperties profileProperties;
  private final Environment env;

  @Value("${profile.name}")
  private String profileName;

  @GetMapping("/check")
  public Map<String, Object> config() {
    Map<String, Object> map = new HashMap<>();
    map.put("profileName", profileName);
    map.put("ProfileProperties profile.name", profileProperties.getName());
    map.put("Environment profile.name", env.getProperty("profile.name"));
    return map;
  }
}
