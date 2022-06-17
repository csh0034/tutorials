package com.ask.springjpacore.repository;

import com.ask.springjpacore.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
