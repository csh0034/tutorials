package com.ask.multitenancy.config;

import com.ask.multitenancy.entity.master.Tenant;
import com.ask.multitenancy.repository.master.TenantRepository;
import com.ask.multitenancy.repository.tenant.CompanyRepository;
import com.ask.multitenancy.tenant.TenantContextHolder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class ScheduleConfig {

  private final TenantRepository tenantRepository;
  private final CompanyRepository companyRepository;

  @Scheduled(fixedRateString = "PT1M")
  public void schedule() {
    List<Tenant> tenants = tenantRepository.findAll();

    tenants.forEach(tenant -> {
      String tenantId = tenant.getId();
      TenantContextHolder.setTenantId(tenantId);

      log.info("======= start: {} =======", tenantId);
      companyRepository.findAll().forEach(company -> log.info("company: {}", company));
      log.info("=======  end : {} =======", tenantId);
    });
  }

}
