package com.ask.multitenancy.config.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

  private static final String DEFAULT_TENANT_ID = "default";

  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenant = TenantContextHolder.getTenantId();
    return StringUtils.hasText(tenant) ? tenant : DEFAULT_TENANT_ID;
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
