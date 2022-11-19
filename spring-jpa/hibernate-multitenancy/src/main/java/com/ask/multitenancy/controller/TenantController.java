package com.ask.multitenancy.controller;

import com.ask.multitenancy.entity.master.Tenant;
import com.ask.multitenancy.entity.tenant.Company;
import com.ask.multitenancy.repository.master.TenantRepository;
import com.ask.multitenancy.repository.tenant.CompanyRepository;
import com.ask.multitenancy.service.CompanyService;
import com.ask.multitenancy.tenant.TenantDatabaseHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TenantController {

  private final TenantRepository tenantRepository;
  private final CompanyService companyService;
  private final CompanyRepository companyRepository;

  private final TenantDatabaseHelper tenantDatabaseHelper;

  @GetMapping("/tenant/create-db")
  public String createTenantDatabase(String tenantId) {
    Tenant tenant = createTenant(tenantId);
    tenantDatabaseHelper.executeSchemaExport(tenant);
    return tenant.getDbName();
  }

  private Tenant createTenant(String tenantId) {
    Tenant tenant = Tenant.builder()
        .id(tenantId)
        .dbName("db_" + tenantId)
        .dbAddress("localhost")
        .dbUsername("root")
        .dbPassword("111111")
        .build();

    return tenantRepository.save(tenant);
  }

  @GetMapping("/companies")
  public ResponseEntity<List<Company>> findAllCompanies() {
    List<Company> companies = companyRepository.findAll();
    companyService.printAllAsync();
    return ResponseEntity.ok(companies);
  }

  @GetMapping("/companies/add")
  public String addCompany() {
    Company company = Company.create("sample-" + System.currentTimeMillis());
    return companyRepository.save(company).getId();
  }

  @GetMapping("/tenant-master-nested")
  public String tenantMaterNested() {
    companyService.nested();
    return "Ok";
  }
}
