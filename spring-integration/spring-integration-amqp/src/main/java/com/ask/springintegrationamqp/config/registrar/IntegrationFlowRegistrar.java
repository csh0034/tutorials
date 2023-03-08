package com.ask.springintegrationamqp.config.registrar;

import org.springframework.integration.dsl.context.IntegrationFlowContext;

public interface IntegrationFlowRegistrar {

  void register(IntegrationFlowContext integrationFlowContext);

}
