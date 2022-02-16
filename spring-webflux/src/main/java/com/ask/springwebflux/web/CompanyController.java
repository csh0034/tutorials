package com.ask.springwebflux.web;

import com.ask.springwebflux.entity.Company;
import com.ask.springwebflux.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @GetMapping
  public Flux<Company> findAll() {
    return companyService.findAll();
  }

}
