package com.ask.multitenancy.runner;

import com.ask.multitenancy.entity.master.Tenant;
import com.ask.multitenancy.tenant.TenantDatabaseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

//@Component
@RequiredArgsConstructor
public class MultiTenantCreateRunner implements ApplicationRunner {

  private final TenantDatabaseHelper tenantDatabaseHelper;

  @Override
  public void run(ApplicationArguments args) {
    Tenant tenant = Tenant.builder()
        .id("sampleId")
        .dbName("db_sample")
        .dbAddress("localhost")
        .dbUsername("root")
        .dbPassword("111111")
        .build();
    tenantDatabaseHelper.executeSchemaExport(tenant);
  }
}
