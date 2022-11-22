package com.ask.multitenancy.tenant;

import com.ask.multitenancy.entity.master.Tenant;
import com.ask.multitenancy.repository.master.TenantRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private static final long serialVersionUID = 2785150572928056660L;

  private final DataSource dataSource;
  private final DataSourceProperties dataSourceProperties;
  private final TenantRepository tenantRepository;

  private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

  @Override
  protected DataSource selectAnyDataSource() {
    log.info("selectAnyDataSource: masterDataSource selected.");
    return dataSource;
  }

  @Override
  protected DataSource selectDataSource(String tenantId) {
    log.info("selectDataSource: tenantDataSource selected. {}", tenantId);

    DataSource dataSource = dataSourceMap.get(tenantId);
    if (dataSource == null) {
      dataSource = createDataSource(tenantId);
      dataSourceMap.put(tenantId, dataSource);
    }
    return dataSource;
  }

  private DataSource createDataSource(String tenantId) {
    if (tenantRepository == null) {
      throw new RuntimeException("tenantRepository must not be null");
    }

    Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new RuntimeException("tenant not found : " + tenantId));
    return dataSourceProperties.initializeDataSourceBuilder()
        .url(tenant.getJdbcUrl())
        .username(tenant.getDbUsername())
        .password(tenant.getDbPassword())
        .build();
  }
}
