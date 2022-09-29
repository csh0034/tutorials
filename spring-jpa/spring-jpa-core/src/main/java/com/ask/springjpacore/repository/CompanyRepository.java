package com.ask.springjpacore.repository;

import com.ask.springjpacore.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyRepository extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {

}
