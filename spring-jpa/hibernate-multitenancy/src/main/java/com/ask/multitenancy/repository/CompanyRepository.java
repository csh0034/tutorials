package com.ask.multitenancy.repository;

import com.ask.multitenancy.entity.base.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
