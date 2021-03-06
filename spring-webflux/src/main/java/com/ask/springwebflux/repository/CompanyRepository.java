package com.ask.springwebflux.repository;

import com.ask.springwebflux.entity.Company;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

  List<Company> findAllByNameContains(String name);

}
