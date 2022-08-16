package com.ask.multitenancy.tenant;

public class TenantContextHolder {

  public static final String DEFAULT_TENANT_ID = "default";

  private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

  public static void setTenantId(String tenantId) {
    CONTEXT_HOLDER.set(tenantId);
  }

  public static String getTenantId() {
    return CONTEXT_HOLDER.get();
  }

  public static void clear() {
    CONTEXT_HOLDER.remove();
  }
}
