package com.ask.springjpaenvers.listener;

import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

@Slf4j
public class TestIntegrator implements Integrator, IntegratorProvider {

  @Override
  public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
      SessionFactoryServiceRegistry serviceRegistry) {
    log.info("call TestIntegrator integrate");
  }

  @Override
  public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
  }

  @Override
  public List<Integrator> getIntegrators() {
    return Collections.singletonList(new TestIntegrator());
  }
}
