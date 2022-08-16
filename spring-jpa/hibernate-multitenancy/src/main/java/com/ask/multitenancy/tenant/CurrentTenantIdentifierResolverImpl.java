package com.ask.multitenancy.tenant;

import java.util.Optional;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

  @Override
  public String resolveCurrentTenantIdentifier() {
    return Optional.ofNullable(TenantContextHolder.getTenantId())
        .orElse(TenantContextHolder.DEFAULT_TENANT_ID);
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
