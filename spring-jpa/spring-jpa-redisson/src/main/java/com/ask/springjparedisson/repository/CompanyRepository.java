package com.ask.springjparedisson.repository;

import com.ask.springjparedisson.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
