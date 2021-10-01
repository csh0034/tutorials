package com.ask.springcore.inject;

import com.ask.springcore.inject.component.ComponentInterface;
import com.ask.springcore.inject.component.NotComponent;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InjectConfig {

  private final List<ComponentInterface> componentInterfaces; // ArrayList size 2
  private final ObjectProvider<ComponentInterface> componentObjectProvider; // DependencyObjectProvider

  private final List<NotComponent> notComponents; // ArrayList size 0
  private final ObjectProvider<NotComponent> notComponentObjectProvider; // DependencyObjectProvider

//  private final NotComponent notComponent0; // NoSuchBeanDefinitionException!!
//
//  @Autowired
//  private NotComponent notComponent1; // NoSuchBeanDefinitionException!!

  @Nullable
  private final NotComponent notComponent2; // null

  private final Optional<NotComponent> notComponent3; // optional.empty

  @Nullable
  @Autowired
  private NotComponent notComponent4; // null

  @Autowired(required = false)
  private NotComponent notComponent5; // null

  @Autowired
  private Optional<NotComponent> notComponent6;// optional.empty

  @Nullable
  @Autowired
  private List<NotComponent> notComponentsAutowiredWithNullable; // null

  @Autowired(required = false)
  private List<NotComponent> notComponentsAutowiredWithFalse; // null

  @PostConstruct
  public void init() {
    log.info("List<ComponentInterface> : {}", componentInterfaces.getClass().getSimpleName());
    componentInterfaces.forEach(ComponentInterface::printName);

    log.info("ObjectProvider<ComponentInterface> : {}", componentObjectProvider.getClass().getSimpleName());
    componentObjectProvider.orderedStream().forEach(ComponentInterface::printName);

    log.info("List<NotComponent> : {}", notComponents.getClass().getSimpleName());
    notComponents.forEach(NotComponent::printName);

    log.info("ObjectProvider<NotComponent> : {}", notComponentObjectProvider.getClass().getSimpleName());
    notComponentObjectProvider.orderedStream().forEach(NotComponent::printName);
  }
}
