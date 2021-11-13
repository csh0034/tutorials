package com.ask.multitenancy.tenant;

import com.ask.multitenancy.entity.master.Tenant;
import com.ask.multitenancy.repository.master.TenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TenantDatabaseHelperTest {

  @Autowired
  private TenantDatabaseHelper tenantDatabaseHelper;

  @Autowired
  private TenantRepository tenantRepository;

  @Test
  void executeSchemaExport() {
    // given
    Tenant tenant = Tenant.builder()
        .id("sampleId")
        .dbName("db_sample")
        .dbAddress("localhost")
        .dbUsername("root")
        .dbPassword("111111")
        .build();

    tenantRepository.save(tenant);

    // when
    tenantDatabaseHelper.executeSchemaExport(tenant);
  }
}