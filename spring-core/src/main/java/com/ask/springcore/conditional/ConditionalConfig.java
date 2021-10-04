package com.ask.springcore.conditional;

import com.ask.springcore.conditional.component.ConditionalComponent;
import com.ask.springcore.conditional.component.DependComponent;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ConditionalConfig {

  @Nullable
  private final ConditionalComponent conditionalService;

  @Nullable
  private final DependComponent dependComponent;

  @PostConstruct
  public void init() {
    log.info("conditionalService : {}", conditionalService);
    log.info("dependComponent : {}", dependComponent);
  }
}
