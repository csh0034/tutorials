package com.ask.springwebflux;

import com.ask.springwebflux.entity.Company;
import com.ask.springwebflux.repository.CompanyRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializeRunner implements ApplicationRunner {

  private final CompanyRepository companyRepository;

  @Override
  public void run(ApplicationArguments args) {
    companyRepository.saveAll(
        Arrays.asList(
            Company.create("company1", "address1"),
            Company.create("company2", "address2"),
            Company.create("company3", "address3"),
            Company.create("company4", "address4"),
            Company.create("company5", "address5")
        )
    );
  }

}
