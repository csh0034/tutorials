package com.ask.springjpacore.web;

import com.ask.springjpacore.entity.Company;
import com.ask.springjpacore.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @PostMapping("/add")
  public String add() {
    Company company = Company.create("company-" + System.currentTimeMillis());
    companyService.save(company);
    return company.getId();
  }

  @PostMapping("/update/{companyId}")
  public String update(@PathVariable String companyId) {
    Company company = companyService.findById(companyId)
        .orElseThrow(() -> new IllegalArgumentException("not found"));

    company.updateName("company-" + System.currentTimeMillis());

    companyService.save(company);

    return "success";
  }

}
