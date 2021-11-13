package com.ask.multitenancy.tenant;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class TenantHeaderFilter implements OrderedFilter {

  public static final String X_TENANT_ID_HEADER = "X-TENANT-ID";

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    String tenantId = request.getHeader(X_TENANT_ID_HEADER);
    log.info("X_TENANT_ID_HEADER : {}", tenantId);

    if (StringUtils.hasText(tenantId)) {
      TenantContextHolder.setTenantId(tenantId);
    }

    filterChain.doFilter(servletRequest, servletResponse);

    TenantContextHolder.clear();
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
