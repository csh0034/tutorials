package com.ask.springjpaenvers.config;

import com.ask.springjpaenvers.listener.CompanyLogWriteListener;
import com.ask.springjpaenvers.service.CompanyLogService;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class JpaConfig  {

  private final EntityManagerFactory emf;

  private final CompanyLogService companyLogService;

  @PostConstruct
  public void init() {
    SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    CompanyLogWriteListener logWriteListener = new CompanyLogWriteListener(companyLogService);

    registry.appendListeners(EventType.POST_INSERT, logWriteListener);
    registry.appendListeners(EventType.POST_UPDATE, logWriteListener);
    registry.appendListeners(EventType.POST_DELETE, logWriteListener);
  }

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.of("ADMIN");
  }

}
