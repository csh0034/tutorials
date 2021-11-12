package com.ask.multitenancy.config.tenant;

public class TenantContextHolder {

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
